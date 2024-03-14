package com.kuba.example.projects.impl

import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.Repository
import com.kuba.example.service.api.ServiceResult
import com.kuba.example.service.api.User

class FakeGithubService: GithubService {

    var searchReposResult: ServiceResult<List<Repository>> = ServiceResult.Success(listOf(fakeRepository))

    companion object {
        val fakeRepository = Repository(
            id = 1,
            name = "Dres-adventures",
            description = "Adventures of Dr. Dre",
            ownerLogin = "Dr-dre",
            stars = 1000
        )
    }

    override suspend fun searchRepos(query: String): ServiceResult<List<Repository>> {
        return searchReposResult
    }

    override suspend fun getContributors(
        login: String,
        repositoryName: String
    ): ServiceResult<List<User>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDetails(login: String): ServiceResult<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserRepos(login: String): ServiceResult<List<Repository>> {
        TODO("Not yet implemented")
    }
}
