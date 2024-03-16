package com.kuba.example.projects.impl.search

import com.kuba.example.service.api.GithubService
import com.kuba.example.service.impl.FailureProvider
import com.kuba.example.service.impl.FakeGithubService
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class RepositorySearchFragmentViewModelTest(
    private val statePermutations: StatePermutations
) {

    @Test
    fun `given a RepositorySearchFragmentViewModel, when search is invoked then correct state is return`() = runTest {
        // Given
        val viewModel = RepositorySearchFragmentViewModel(statePermutations.githubService)
        val collectJob = launch { viewModel.state.value }
        // assert that the initial state is idle
        assert(viewModel.state.value is RepositoriesUiModel.Idle)

        // When
        viewModel.search(statePermutations.query)

        // Then
        val newState = viewModel.state.value
        statePermutations.thenBlock(newState)
        collectJob.cancel()
    }

    companion object {
        private fun providerFailureProvider(
            forced: Boolean = false
        ): FailureProvider = object : FailureProvider { override val forceFailure: Boolean = forced }

        enum class StatePermutations(
            val query: String = "query",
            val githubService: GithubService = FakeGithubService(providerFailureProvider()),
            val thenBlock: (RepositoriesUiModel) -> Unit
        ) {
            FAILURE(
                githubService = FakeGithubService(providerFailureProvider(true)),
                thenBlock = { state ->
                    assert((state as RepositoriesUiModel.Error).message == "Error: Network error, please try again later.")
                }
            ),
            ERROR(
                query = " ",
                thenBlock = { state ->
                    assert((state as RepositoriesUiModel.Error).message == "Please enter a search query")
                }
            ),
            CONTENT(
                thenBlock = { state ->
                    val repositories = (state as RepositoriesUiModel.Content).repositories
                    val expected = FakeGithubService.fakeRepositories
                    assert(repositories == expected)
                }
            )
        }

        @JvmStatic
        @Parameterized.Parameters
        fun permutations(): Collection<Array<Any>> {
            return listOf(
                arrayOf(StatePermutations.FAILURE),
                arrayOf(StatePermutations.ERROR),
                arrayOf(StatePermutations.CONTENT)
            )
        }
    }
}
