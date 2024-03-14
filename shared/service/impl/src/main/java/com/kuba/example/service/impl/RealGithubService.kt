package com.kuba.example.service.impl

import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.Repository
import com.kuba.example.service.api.ServiceResult
import com.kuba.example.service.api.User
import com.kuba.example.service.api.UserDetails
import com.kuba.example.service.impl.data.api.GithubApi
import com.kuba.example.service.impl.data.mapping.mapToRepository
import com.kuba.example.service.impl.data.mapping.mapToUser
import com.kuba.example.service.impl.data.mapping.mapToUserDetails
import com.slack.eithernet.ApiResult
import javax.inject.Inject

class RealGithubService @Inject constructor(private val githubApi: GithubApi) : GithubService {
    override suspend fun searchRepos(query: String): ServiceResult<List<Repository>> {
        return when (val searchResult = githubApi.searchRepos(query)) {
            is ApiResult.Success -> {
                ServiceResult.Success(searchResult.value.items.map { it.mapToRepository() })
            }

            is ApiResult.Failure -> searchResult.mapToServiceResult()
        }
    }

    override suspend fun getContributors(
        login: String,
        repositoryName: String
    ): ServiceResult<List<User>> {
        return when (val result =
            githubApi.getContributors(owner = login, name = repositoryName)) {
            is ApiResult.Success -> {
                ServiceResult.Success(result.value.map { it.mapToUser() })
            }

            is ApiResult.Failure -> result.mapToServiceResult()
        }
    }

    override suspend fun getUserDetails(login: String): ServiceResult<UserDetails> {
        return when (val result =
            githubApi.getUser(login = login)) {
            is ApiResult.Success -> {
                ServiceResult.Success(result.value.mapToUserDetails())
            }

            is ApiResult.Failure -> result.mapToServiceResult()
        }
    }

    override suspend fun getUserRepos(login: String): ServiceResult<List<Repository>> {
        return when (val result =
            githubApi.getRepos(login)) {
            is ApiResult.Success -> {
                ServiceResult.Success(result.value.map { it.mapToRepository() })
            }

            is ApiResult.Failure -> result.mapToServiceResult()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> ApiResult.Failure<*>.mapToServiceResult(): ServiceResult<T> {
        return when (this) {
            is ApiResult.Failure.NetworkFailure -> ServiceResult.Failure(reason = "Network error, please try again")
            is ApiResult.Failure.UnknownFailure -> ServiceResult.Failure(
                reason = "Unknown failure",
                throwable = error
            )

            is ApiResult.Failure.HttpFailure -> ServiceResult.Failure(reason = "Server error: ${this.code}")
            is ApiResult.Failure.ApiFailure -> ServiceResult.Failure(reason = "Json parsing error")
        }
    }
}
