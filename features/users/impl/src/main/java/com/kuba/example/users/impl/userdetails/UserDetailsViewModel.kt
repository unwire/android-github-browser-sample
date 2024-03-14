package com.kuba.example.users.impl.userdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.ServiceResult
import com.kuba.example.users.api.navigation.UserDetailsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val githubService: GithubService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state: MutableStateFlow<UserDetailsUiModel> =
        MutableStateFlow(UserDetailsUiModel.Loading)
    val state: StateFlow<UserDetailsUiModel> = _state

    private val userLogin get() = UserDetailsScreen.extractUserLogin(savedStateHandle)

    /**
     * Get user details
     */
    suspend fun loadUserDetails() {
        val result = githubService.getUserDetails(userLogin)
        val uiModel = when (result) {
            is ServiceResult.Success -> UserDetailsUiModel.Content(result.value)
            is ServiceResult.Failure -> UserDetailsUiModel.Error("Error: ${result.reason ?: result.throwable?.message ?: "unknown"}")
        }

        _state.value = uiModel
    }
}
