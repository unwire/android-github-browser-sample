package com.kuba.example.users.impl.userdetails

import com.kuba.example.service.api.UserDetails

/**
 * The UI data to be rendered on the screen
 */
sealed class UserDetailsUiModel {

    data object Loading : UserDetailsUiModel()

    data class Content(val userDetails: UserDetails) : UserDetailsUiModel()

    data class Error(val message: String) : UserDetailsUiModel()
}
