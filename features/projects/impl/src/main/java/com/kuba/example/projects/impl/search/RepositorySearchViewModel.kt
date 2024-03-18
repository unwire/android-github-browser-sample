package com.kuba.example.projects.impl.search

import androidx.lifecycle.ViewModel
import com.kuba.example.dagger.conductor.scope.ControllerScope
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.Repository
import com.kuba.example.service.api.ServiceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Deprecated("Will be deleted shortly, use RepositorySearchFragmentViewModel instead")
@ControllerScope(RepositorySearchController::class)
class RepositorySearchViewModel @Inject constructor(override val getGithubService: GithubService) : BaseRepositorySearchViewModel()

@HiltViewModel
class RepositorySearchFragmentViewModel @Inject constructor(override val getGithubService: GithubService) : BaseRepositorySearchViewModel()

abstract class BaseRepositorySearchViewModel : ViewModel() {
    abstract val getGithubService: GithubService
    private val _state: MutableStateFlow<RepositoriesUiModel> = MutableStateFlow(RepositoriesUiModel.Idle)
    val state: StateFlow<RepositoriesUiModel> = _state

    /**
     * Search Github repositories
     */
    suspend fun search(query: String) = getGithubService.searchRepos(query)
        .map { it.toUiModel() }
        .collect { _state.value = it }

    private fun ServiceResult<List<Repository>>.toUiModel(): RepositoriesUiModel {
        return when (this) {
            is ServiceResult.Success -> {
                if (value.isEmpty()) {
                    RepositoriesUiModel.Error("No repositories found") // A string resource should be used here
                } else {
                    RepositoriesUiModel.Content(value)
                }
            }

            is ServiceResult.Failure -> RepositoriesUiModel.Error(reason ?: "An error occurred") // A string resource should be used here
            ServiceResult.Loading -> RepositoriesUiModel.Loading
        }
    }
}