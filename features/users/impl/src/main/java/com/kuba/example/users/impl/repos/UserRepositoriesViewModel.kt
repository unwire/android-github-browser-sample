package com.kuba.example.users.impl.repos

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.ServiceResult
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserRepositoriesViewModel @Inject constructor(
    private val githubService: GithubService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state: MutableStateFlow<UserRepositoriesUiModel> =
        MutableStateFlow(UserRepositoriesUiModel.Content(emptyList()))
    val state: StateFlow<UserRepositoriesUiModel> = _state

    private val user get() = UserRepositoriesScreen.extractUser(savedStateHandle)

    /**
     * Search Github repositories
     */
    suspend fun loadRepositories() {
        val result = githubService.getUserRepos(user.login)
        val uiModel = when (result) {
            is ServiceResult.Success -> UserRepositoriesUiModel.Content(result.value)
            is ServiceResult.Failure -> UserRepositoriesUiModel.Error("Error: ${result.reason ?: result.throwable?.message ?: "unknown"}")
        }

        _state.value = uiModel
    }
}
