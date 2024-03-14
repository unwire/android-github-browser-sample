package com.kuba.example.projects.impl

import com.kuba.example.projects.impl.FakeGithubService.Companion.fakeRepository
import com.kuba.example.projects.impl.search.RepositoriesUiModel
import com.kuba.example.projects.impl.search.RepositorySearchViewModel
import com.kuba.example.service.api.ServiceResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositorySearchViewModelTest {

    private val fakeGithubService = FakeGithubService()
    private val viewModel = RepositorySearchViewModel(fakeGithubService)

    @Test
    fun verify_stateIsSetCorrectly_toSuccessWithData_when_GithubService_returns_aListOfRepositories() =
        runBlocking {
            // Given GithubService returns a list of repos for a given query
            val fakeRepositories = listOf(fakeRepository)
            fakeGithubService.searchReposResult = ServiceResult.Success(fakeRepositories)

            // When search is invoked
            viewModel.search(fakeRepository.name)

            // Then the state is set correctly with the list of repos returned
            assertEquals(RepositoriesUiModel.Content(fakeRepositories), viewModel.state.value)
        }

    @Test
    fun verify_stateIsSetCorrectly_toSuccessWithNoData_when_GithubService_returns_anEmptyList() =
        runBlocking {
            // Given GithubService returns an empty list for a given query
            fakeGithubService.searchReposResult = ServiceResult.Success(emptyList())

            // When search is invoked
            viewModel.search("Random repo that does not exist")

            // Then the state is set correctly with an empty list
            assertEquals(RepositoriesUiModel.Content(emptyList()), viewModel.state.value)
        }

    @Test
    fun verify_stateIsSetCorrectly_toError_when_GithubService_returns_aFailure() =
        runBlocking {
            // Given GithubService returns a failure
            fakeGithubService.searchReposResult = ServiceResult.Failure("Something went wrong")

            // When search is invoked
            viewModel.search("Dres-adventures")

            // Then the state is set correctly with an error
            assert(viewModel.state.value is RepositoriesUiModel.Error)
        }

    @Test
    fun verify_stateIsSetCorrectly_toError_when_searchIsInvoked_withAnEmptyQuery() =
        runBlocking {
            // Given user has not provided a search query
            val query = ""

            // When search is invoked
            viewModel.search(query)

            // Then the state is set correctly with an empty list
            assert(viewModel.state.value is RepositoriesUiModel.Error)
        }
}
