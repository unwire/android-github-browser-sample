package com.kuba.example.githubbrowser

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.kuba.example.dagger.conductor.HasControllerInjector
import com.kuba.example.dagger.conductor.HasControllerInjectorProvider
import com.kuba.example.githubbrowser.databinding.ActivityMainBinding
import com.kuba.example.navigation.api.ControllerFactory
import com.kuba.example.navigation.api.FeatureFlagFactory
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.api.navigation.RepositorySearchScreen
import com.kuba.example.projects.impl.contributors.ContributorsFragment
import com.kuba.example.projects.impl.search.RepositorySearchFragment
import com.kuba.example.users.api.navigation.UserDetailsScreen
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
import com.kuba.example.users.impl.repos.UserDetailsFragment
import com.kuba.example.users.impl.repos.UserRepositoriesFragment
import dagger.android.DispatchingAndroidInjector
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

// TODO: remove HasControllerInjectorProvider & HasControllerInjector when all controllers are migrated to Android Navigation
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HasControllerInjector, HasControllerInjectorProvider {
    private lateinit var binding: ActivityMainBinding

    private lateinit var router: Router

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var featureFlagFactory: FeatureFlagFactory

    @Inject
    internal lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Controller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("onCreate, savedInstanceState: $savedInstanceState")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (isAndroidNavigationEnabled()) {
            binding.setupAndroidNavigation()
        } else {
            binding.setupConductor(savedInstanceState)
        }
    }

    override fun onDestroy() {
        Timber.d("onDestroy")
        super.onDestroy()
    }

    override fun controllerInjector(): DispatchingAndroidInjector<Controller> {
        return dispatchingAndroidInjector
    }
    override fun controllerInjector(controller: Controller): HasControllerInjector {
        return this
    }

    @CallSuper
    override fun onBackPressed() {
        if (isAndroidNavigationEnabled()) {
            super.onBackPressed()
            return
        }
        if (router != null) {
            // must delegate BACK press to routers
            if (!router!!.handleBack()) {
                super.onBackPressed()
            }
        } else {
            Timber.e ("onBackPressed called while router was null." )
        }
    }

    private fun ActivityMainBinding.setupConductor(savedInstanceState: Bundle?) {
        with(controllerContainer) {
            visibility = View.VISIBLE
            router = Conductor.attachRouter(this@MainActivity, binding.controllerContainer, savedInstanceState)
                .setPopRootControllerMode(Router.PopRootControllerMode.NEVER)

            if (!router.hasRootController()) {
                val controller = controllerFactory.create(RepositorySearchScreen())
                router.setRoot(RouterTransaction.with((controller)))
            }
        }
        with(navHost) { visibility = View.GONE }
    }

    private fun ActivityMainBinding.setupAndroidNavigation() {
        with(controllerContainer) { visibility = View.GONE }
        with(navHost) {
            visibility = View.VISIBLE
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
            navHostFragment.view?.visibility = View.VISIBLE
            val navController = navHostFragment.navController
            navController.graph = navController.createGraph(
                startDestination = RepositorySearchScreen.SEARCH_REPOSITORY_ROUTE
            ) {
                fragment<RepositorySearchFragment>(route = RepositorySearchScreen.SEARCH_REPOSITORY_ROUTE){
                    label = context.getString(R.string.search_repositories)
                }
                fragment<ContributorsFragment>(route = "${ContributorsScreen.CONTRIBUTORS_ROUTE}/{${ContributorsScreen.KEY_ARGS}}"){
                    argument(ContributorsScreen.KEY_ARGS) { type = ContributorsScreen.CONTRIBUTOR_SCREEN_ARGS_TYPE }
                    label = context.getString(R.string.contributors)
                }
                fragment<UserRepositoriesFragment>(route = "${UserRepositoriesScreen.USER_REPOSITORIES_ROUTE}/{${UserRepositoriesScreen.KEY_USER}}"){
                    argument(UserRepositoriesScreen.KEY_USER) { type = UserRepositoriesScreen.USER_NAV_TYPE }
                    label = context.getString(R.string.user_repositories)
                }
                fragment<UserDetailsFragment>(route = "${UserDetailsScreen.USER_DETAILS_REPOSITORIES_ROUTE}/{${UserDetailsScreen.KEY_USER}}"){
                    argument(UserDetailsScreen.KEY_USER) { type = NavType.StringType}
                    label = context.getString(R.string.user_details)
                }
            }
            onBackPressedDispatcher.addCallback(
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (!navController.popBackStack()) {
                            this.isEnabled = false
                            onBackPressed()
                        }
                    }
                }
            )
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        }
    }

    private fun isAndroidNavigationEnabled(): Boolean = featureFlagFactory.isAndroidNavigationEnabled()
}