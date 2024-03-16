package com.kuba.example.projects.impl.search

import com.kuba.example.service.api.Repository

/**
 * The UI data to be rendered on the screen
 */
sealed class RepositoriesUiModel {
    data object Idle : RepositoriesUiModel()
    data object Loading : RepositoriesUiModel()
    data class Content(val repositories: List<Repository>) : RepositoriesUiModel()
    data class Error(val message: String) : RepositoriesUiModel()
}