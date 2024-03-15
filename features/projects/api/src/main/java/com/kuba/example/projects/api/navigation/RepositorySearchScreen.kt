package com.kuba.example.projects.api.navigation

import android.os.Bundle
import com.kuba.example.navigation.api.ControllerDestination

class RepositorySearchScreen : ControllerDestination {
    override val route: String = SEARCH_REPOSITORY_ROUTE
    override val args: Bundle? = null

    companion object {
        const val SEARCH_REPOSITORY_ROUTE = "repos/search"
    }
}