package com.kuba.example.users.impl.repos

import com.kuba.example.service.api.User

sealed class UserDetailsUiModel {
    data object Idle : UserDetailsUiModel()
    data object Loading : UserDetailsUiModel()
    data class Content(val user: User) : UserDetailsUiModel()
    data class Error(val message: String) : UserDetailsUiModel()
}