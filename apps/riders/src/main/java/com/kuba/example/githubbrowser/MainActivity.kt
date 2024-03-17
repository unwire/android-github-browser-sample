package com.kuba.example.githubbrowser

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.api.navigation.RepositorySearchScreen
import com.kuba.example.projects.impl.contributors.ContributorsFragment
import com.kuba.example.projects.impl.search.RepositorySearchFragment
import com.kuba.example.users.api.navigation.UserDetailsScreen
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
import com.kuba.example.users.impl.repos.UserDetailsFragment
import com.kuba.example.users.impl.repos.UserRepositoriesFragment
import dagger.hilt.android.AndroidEntryPoint
import io.kamara.riders.R
import io.kamara.riders.databinding.ActivityMainBinding
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate, savedInstanceState: $savedInstanceState")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(findViewById(R.id.toolbar))
        binding.setupAndroidNavigation()
    }

    private fun ActivityMainBinding.setupAndroidNavigation() {
        with(navHost) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
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
}
