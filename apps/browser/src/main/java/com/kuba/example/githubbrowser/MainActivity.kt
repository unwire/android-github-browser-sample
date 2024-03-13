package com.kuba.example.githubbrowser

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.fragment
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.kuba.example.dagger.conductor.HasControllerInjector
import com.kuba.example.dagger.conductor.HasControllerInjectorProvider
import com.kuba.example.githubbrowser.databinding.ActivityMainBinding
import com.kuba.example.navigation.api.ControllerFactory
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.api.navigation.RepositorySearchScreen
import com.kuba.example.projects.impl.contributors.ContributorsFragment
import com.kuba.example.projects.impl.search.RepositorySearchFragment
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
import com.kuba.example.users.impl.repos.UserRepositoriesFragment
import dagger.android.DispatchingAndroidInjector
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), HasControllerInjector, HasControllerInjectorProvider {
    private lateinit var binding: ActivityMainBinding

    private lateinit var router: Router

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    internal lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Controller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("onCreate, savedInstanceState: $savedInstanceState")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Conductor init
        router = Conductor.attachRouter(this, binding.controllerContainer, savedInstanceState)
                .setPopRootControllerMode(Router.PopRootControllerMode.NEVER)

        if (!router.hasRootController()) {
            val controller = controllerFactory.create(RepositorySearchScreen())
            router.setRoot(RouterTransaction.with((controller)))
        }

        // Get a reference to the NavController for our NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()

        // Build a navigation graph including all the screens in the app
        val navigationGraph = navController.createGraph(
            startDestination = RepositorySearchScreen().route
        ) {
            fragment<RepositorySearchFragment>(RepositorySearchScreen.ROUTE)
            fragment<ContributorsFragment>("${ContributorsScreen.ROUTE}/{${ContributorsScreen.KEY_ARGS}}") {
                argument(ContributorsScreen.KEY_ARGS) {
                    type = contributorScreenArgsType
                }
            }
            fragment<UserRepositoriesFragment>("${UserRepositoriesScreen.ROUTE}/{${UserRepositoriesScreen.KEY_USER}}") {
                argument(UserRepositoriesScreen.KEY_USER) {
                    type = userRepositoriesType
                }
            }
        }
        // TODO: Set navigationGraph as navController.graph
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
        if (router != null) {
            // must delegate BACK press to routers
            if (!router!!.handleBack()) {
                super.onBackPressed()
            }
        } else {
            Timber.e ("onBackPressed called while router was null." )
        }
    }
}
