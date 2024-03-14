package com.kuba.example.projects.impl.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.fragment
import com.kuba.example.projects.api.navigation.RepositorySearchScreen

fun NavGraphBuilder.repositorySearchDestination() {
    fragment<RepositorySearchFragment>(RepositorySearchScreen.ROUTE)
}
