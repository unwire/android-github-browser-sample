package com.kuba.example.projects.impl.contributors

import com.kuba.example.service.api.User

/**
 * The UI data to be rendered on the screen
 */
sealed class ContributorsUiModel {

    data class Content(val repositories: List<User>) : ContributorsUiModel()

    data class Error(val message: String) : ContributorsUiModel()
}