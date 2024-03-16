package com.kuba.example.projects.impl.search

import androidx.lifecycle.ViewModel
import com.kuba.example.dagger.conductor.scope.ControllerScope
import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.ServiceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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
    suspend fun search(query: String) {
       flow {
            emit(RepositoriesUiModel.Loading)
           if (query.isBlank()) {
               emit(RepositoriesUiModel.Error("Please enter a search query"))
           } else {
               val uiModel = when (val result = getGithubService.searchRepos(query)) {
                   is ServiceResult.Success -> RepositoriesUiModel.Content(result.value)
                   is ServiceResult.Failure -> RepositoriesUiModel.Error("Error: ${result.reason ?: result.throwable?.message ?: "unknown"}")
               }
               emit(uiModel)
           }
        }.collect {
            _state.value = it
        }
    }
}