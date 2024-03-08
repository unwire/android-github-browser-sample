package com.kuba.example.users.impl.repos

import com.kuba.example.service.api.Repository

/**
 * The UI data to be rendered on the screen
 */
sealed class UserRepositoriesUiModel {

    data class Content(val repositories: List<Repository>) : UserRepositoriesUiModel()

    data class Error(val message: String) : UserRepositoriesUiModel()
}