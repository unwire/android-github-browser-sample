package com.kuba.example.githubbrowser

import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.Repository
import com.kuba.example.service.api.ServiceResult
import com.kuba.example.service.api.User
import javax.inject.Inject

class FakeGithubService @Inject constructor() : GithubService {
    companion object {
        val fakeUser = User(
            login = "dr-dre",
            name = "Dr. Dre",
            avatarUrl = null
        )

        val fakeRepository = Repository(
            id = 1,
            name = "Dres-adventures",
            description = "Adventures of Dr. Dre",
            ownerLogin = "Dr-dre",
            stars = 1000
        )
    }

    override suspend fun searchRepos(query: String): ServiceResult<List<Repository>> {
        val repositories = listOf(fakeRepository)
        return ServiceResult.Success(repositories)
    }

    override suspend fun getContributors(
        login: String,
        repositoryName: String
    ): ServiceResult<List<User>> {
        val contributors = listOf(fakeUser)
        return ServiceResult.Success(contributors)
    }

    override suspend fun getUserDetails(login: String): ServiceResult<User> {
        return ServiceResult.Success(fakeUser)
    }

    override suspend fun getUserRepos(login: String): ServiceResult<List<Repository>> {
        val repositories = listOf(fakeRepository)
        return ServiceResult.Success(repositories)
    }

}
