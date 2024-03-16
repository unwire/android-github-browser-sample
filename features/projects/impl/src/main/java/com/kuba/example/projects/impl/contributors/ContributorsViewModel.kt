package com.kuba.example.projects.impl.contributors

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kuba.example.dagger.conductor.scope.ControllerScope
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.ServiceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ControllerScope(ContributorsController::class)
class ContributorsViewModel @Inject constructor(
    override val contributorsScreenArgs: ContributorsScreen.ContributorScreenArgs,
    override val githubService: GithubService
) : BaseContributorsViewModel()

@HiltViewModel
class ContributorsFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    override val githubService: GithubService
) : BaseContributorsViewModel() {
    override val contributorsScreenArgs get() = ContributorsScreen.extractArgs(savedStateHandle)
}

abstract class BaseContributorsViewModel : ViewModel() {
    abstract val githubService: GithubService
    abstract val contributorsScreenArgs: ContributorsScreen.ContributorScreenArgs

    private val _state: MutableStateFlow<ContributorsUiModel> =
        MutableStateFlow(ContributorsUiModel.Content(emptyList()))
    val state: StateFlow<ContributorsUiModel> = _state

    /**
     * Search Github repositories
     */
    suspend fun loadContributors() {
        val uiModel = when (val result = githubService.getContributors(contributorsScreenArgs.login, contributorsScreenArgs.repoName)) {
            is ServiceResult.Success -> ContributorsUiModel.Content(result.value)
            is ServiceResult.Failure -> ContributorsUiModel.Error("Error: ${result.reason ?: result.throwable?.message ?: "unknown"}")
        }

        _state.value = uiModel
    }
}