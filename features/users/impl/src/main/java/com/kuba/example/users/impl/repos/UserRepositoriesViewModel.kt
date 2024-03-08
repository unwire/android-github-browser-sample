package com.kuba.example.users.impl.repos

import androidx.lifecycle.ViewModel
import com.kuba.example.dagger.conductor.scope.ControllerScope
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.ServiceResult
import com.kuba.example.service.api.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ControllerScope(UserRepositoriesController::class)
class UserRepositoriesViewModel @Inject constructor(
    private val user: User, // example of injecting controller-scoped dependencies
    private val githubService: GithubService
) :
    ViewModel() {

    private val _state: MutableStateFlow<UserRepositoriesUiModel> =
        MutableStateFlow(UserRepositoriesUiModel.Content(emptyList()))
    val state: StateFlow<UserRepositoriesUiModel> = _state

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