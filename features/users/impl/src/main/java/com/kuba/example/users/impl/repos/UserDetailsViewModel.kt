package com.kuba.example.users.impl.repos

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
    private val savedStateHandle: SavedStateHandle,
    private val githubService: GithubService
) : ViewModel() {
    private val _state: MutableStateFlow<UserDetailsUiModel> =
        MutableStateFlow(UserDetailsUiModel.Loading)
    val state: StateFlow<UserDetailsUiModel> = _state

    suspend fun loadUserDetails() {
        val login = UserDetailsScreen.extractSavedStateHandle(savedStateHandle)
        val uiModel = when (val result = githubService.getUserDetails(login)) {
            is ServiceResult.Success -> UserDetailsUiModel.Content(result.value)
            is ServiceResult.Failure -> UserDetailsUiModel.Error("Error: ${result.reason ?: result.throwable?.message ?: "unknown"}")
            ServiceResult.Loading -> { UserDetailsUiModel.Loading }
        }
        _state.value = uiModel
    }
}


