package com.kuba.example.service.impl

import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.Repository
import com.kuba.example.service.api.ServiceResult
import com.kuba.example.service.api.User
import kotlinx.coroutines.delay
import java.util.Random
import javax.inject.Inject

class FakeGithubService @Inject constructor(
    private val failureProvider: FailureProvider,
) : GithubService {

    override suspend fun searchRepos(
        query: String
    ): ServiceResult<List<Repository>> {
        delay(500) // Simulate network delay
        failureProvider.let {
            if (it.forceFailure) {
                return ServiceResult.Failure(reason = "Network error, please try again later.")
            }
        }
        return ServiceResult.Success(fakeRepositories)
    }

    override suspend fun getContributors(
        login: String,
        repositoryName: String
    ): ServiceResult<List<User>?> = fakeContributors
        .take(random.nextInt(fakeContributors.size))
        .let { return ServiceResult.Success(it) }

    override suspend fun getUserDetails(
        login: String
    ): ServiceResult<User> = ServiceResult.Success(fakeUser1)

    override suspend fun getUserRepos(
        login: String
    ): ServiceResult<List<Repository>?> = fakeRepositories
        .take(random.nextInt(fakeRepositories.size))
        .let { return ServiceResult.Success(it) }

    companion object {
        private val fakeUser1 = User(
            login = "kamara",
            name = "Kamara",
            avatarUrl = "https://imgur.com/1.png"
        )
        private val fakeUser2 = User(
            login = "saidu",
            name = "Saidu",
            avatarUrl = null
        )
        private val fakeUser3 = User(
            login = "other",
            name = "Other",
            avatarUrl = null
        )
        private val fakeRepository1 = Repository(
            id = 1,
            name = "GitHub Octocat",
            description = "Some description",
            ownerLogin = fakeUser1.login,
            stars = 99
        )
        private val fakeRepository2 = Repository(
            id = 2,
            name = "GitHub Octocat",
            description = "Some description",
            ownerLogin = fakeUser2.login,
            stars = 99
        )
        private val fakeRepository3 = Repository(
            id = 3,
            name = "Other Repo",
            description = "Other description",
            ownerLogin = "other",
            stars = 0
        )
        val fakeRepositories = listOf(fakeRepository1, fakeRepository2, fakeRepository3)
        private val fakeContributors = listOf(fakeUser1, fakeUser2, fakeUser3)
        private val random: Random = Random()
    }
}
