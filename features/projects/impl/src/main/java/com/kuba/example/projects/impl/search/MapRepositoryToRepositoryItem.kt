package com.kuba.example.projects.impl.search

import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.service.api.Repository

class MapRepositoryToRepositoryItem(
    private val itemClickListener:  ((ContributorsScreen) -> Unit)? = null
){
    operator fun invoke(repositories: List<Repository>): List<RepositoryItem> {
        return repositories.map {
            RepositoryItem(
                owner = it.ownerLogin,
                name = it.name,
                description = it.description,
                stars = it.stars
            ).apply {
                onClickListener = itemClickListener
            }
        }
    }
}