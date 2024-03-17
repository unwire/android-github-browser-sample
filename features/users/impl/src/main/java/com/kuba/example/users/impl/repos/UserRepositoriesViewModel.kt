package com.kuba.example.users.impl.repos

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kuba.example.dagger.conductor.scope.ControllerScope
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.ServiceResult
import com.kuba.example.service.api.User
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@Deprecated("Will be deleted, Use UserRepositoriesFragmentViewModel instead")
@ControllerScope(UserRepositoriesController::class)
class UserRepositoriesViewModel @Inject constructor(
    override val user: User, // example of injecting controller-scoped dependencies
    override val githubService: GithubService
) : BaseContributorsViewModel()

@HiltViewModel
class UserRepositoriesFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    override val githubService: GithubService
) : BaseContributorsViewModel() {
    override val user get() = UserRepositoriesScreen.extractUserFromSavedStateHandle(savedStateHandle)
}

abstract class BaseContributorsViewModel : ViewModel() {
    abstract val githubService: GithubService
    abstract val user: User

    private val _state: MutableStateFlow<UserRepositoriesUiModel> =
        MutableStateFlow(UserRepositoriesUiModel.Content(emptyList()))
    val state: StateFlow<UserRepositoriesUiModel> = _state

    /**
     * Search Github repositories
     */
    suspend fun loadRepositories() {
        flow {
            emit(UserRepositoriesUiModel.Loading)
            val uiModel = when (val result = githubService.getUserRepos(user.login)) {
                is ServiceResult.Success -> UserRepositoriesUiModel.Content(result.value ?: emptyList())
                is ServiceResult.Failure -> UserRepositoriesUiModel.Error("Error: ${result.reason ?: result.throwable?.message ?: "unknown"}")
            }
            emit(uiModel)
        }.collect {
            _state.value = it
        }
    }
}