package com.kuba.example.projects.impl.contributors

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.ServiceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ContributorsViewModel @Inject constructor(
    private val githubService: GithubService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state: MutableStateFlow<ContributorsUiModel> =
        MutableStateFlow(ContributorsUiModel.Content(emptyList()))
    val state: StateFlow<ContributorsUiModel> = _state

    private val contributorsScreenArgs get() = ContributorsScreen.extractArgs(savedStateHandle)

    /**
     * Search Github repositories
     */
    suspend fun loadContributors() {
        val result = githubService.getContributors(
            contributorsScreenArgs.login,
            contributorsScreenArgs.repoName
        )
        val uiModel = when (result) {
            is ServiceResult.Success -> ContributorsUiModel.Content(result.value)
            is ServiceResult.Failure -> ContributorsUiModel.Error("Error: ${result.reason ?: result.throwable?.message ?: "unknown"}")
        }

        _state.value = uiModel
    }
}
