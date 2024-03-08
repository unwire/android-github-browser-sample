package com.kuba.example.projects.impl.contributors

import androidx.lifecycle.ViewModel
import com.kuba.example.dagger.conductor.scope.ControllerScope
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.ServiceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ControllerScope(ContributorsController::class)
class ContributorsViewModel @Inject constructor(
    private val contributorsScreenArgs: ContributorsScreen.ContributorScreenArgs, // example of injecting controller-scoped dependencies
    private val githubService: GithubService
) :
    ViewModel() {

    private val _state: MutableStateFlow<ContributorsUiModel> =
        MutableStateFlow(ContributorsUiModel.Content(emptyList()))
    val state: StateFlow<ContributorsUiModel> = _state

    /**
     * Search Github repositories
     */
    suspend fun loadContributors() {
        val result = githubService.getContributors(contributorsScreenArgs.login, contributorsScreenArgs.repoName)
        val uiModel = when (result) {
            is ServiceResult.Success -> ContributorsUiModel.Content(result.value)
            is ServiceResult.Failure -> ContributorsUiModel.Error("Error: ${result.reason ?: result.throwable?.message ?: "unknown"}")
        }

        _state.value = uiModel
    }
}