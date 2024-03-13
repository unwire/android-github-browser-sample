package com.kuba.example.githubbrowser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.fragment
import com.kuba.example.githubbrowser.databinding.ActivityMainBinding
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.api.navigation.RepositorySearchScreen
import com.kuba.example.projects.impl.contributors.ContributorsFragment
import com.kuba.example.projects.impl.search.RepositorySearchFragment
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
import com.kuba.example.users.impl.repos.UserRepositoriesFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("onCreate, savedInstanceState: $savedInstanceState")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        navController.graph = navigationGraph
    }

    override fun onDestroy() {
        Timber.d("onDestroy")
        super.onDestroy()
    }
}
