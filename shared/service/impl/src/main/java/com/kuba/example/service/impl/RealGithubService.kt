package com.kuba.example.service.impl

import com.kuba.example.service.api.GithubService
import com.kuba.example.service.api.Repository
import com.kuba.example.service.api.ServiceResult
import com.kuba.example.service.api.User
import com.kuba.example.service.impl.data.api.GithubApi
import com.kuba.example.service.impl.data.db.RepositoryItem
import com.kuba.example.service.impl.data.db.RepositoryItemDao
import com.kuba.example.service.impl.data.db.mapToRepository
import com.kuba.example.service.impl.data.db.mapToRepositoryItem
import com.kuba.example.service.impl.data.mapping.mapToRepository
import com.kuba.example.service.impl.data.mapping.mapToUser
import com.slack.eithernet.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RealGithubService @Inject constructor(
    private val githubApi: GithubApi,
    private val repositoryItemDao: RepositoryItemDao
) : GithubService {

    override fun searchRepos(
        query: String,
    ): Flow<ServiceResult<List<Repository>>> = flow {
        emit(ServiceResult.Loading)
        val cachedRepos: MutableList<RepositoryItem> = mutableListOf()
        try {
            if (query.isBlank()) {
                emit(ServiceResult.Failure("Search query cannot be empty"))
                return@flow
            }
            // Search from cached data
            cachedRepos.addAll(repositoryItemDao.searchRepos(query))
            if (cachedRepos.isNotEmpty()) {
                emit(ServiceResult.Success(cachedRepos.mapToRepository()))
            }
            // Fetch data from network
            when (val searchResult = githubApi.searchRepos(query)) {
                is ApiResult.Success -> {
                    val repositories = searchResult.value.items.map { it.mapToRepository() }
                    // Insert new data into cache
                    for (repository in repositories) {
                        repositoryItemDao.insertRepository(repository.mapToRepositoryItem())
                    }
                    // Emit network data
                    emit(ServiceResult.Success(repositoryItemDao.searchRepos(query).mapToRepository()))
                }
                is ApiResult.Failure -> {
                    // Emit cached data only if network call fails
                    if (cachedRepos.isEmpty()) {
                        emit(searchResult.mapToServiceResult())
                    }
                }
            }
        } catch (e: Exception) {
            // Emit error if both cache and network fail
            if (cachedRepos.isEmpty()) {
                emit(e.mapToServiceResult("repositories"))
            }
        }
    }

    override suspend fun getContributors(
        login: String,
        repositoryName: String,
    ): ServiceResult<List<User>?> {
        return try {
            when (val result = githubApi.getContributors(owner = login, name = repositoryName)) {
                is ApiResult.Success -> ServiceResult.Success(result.value.map { it.mapToUser() })
                is ApiResult.Failure -> result.mapToServiceResult()
            }
        } catch (e: Exception) {
            e.mapToServiceResult("contributors")
        }
    }

    override suspend fun getUserDetails(
        login: String,
    ): ServiceResult<User> {
        return when (val result = githubApi.getUser(login)) {
            is ApiResult.Success -> ServiceResult.Success(result.value.mapToUser())
            is ApiResult.Failure -> result.mapToServiceResult()
        }
    }

    override suspend fun getUserRepos(
        login: String,
    ): ServiceResult<List<Repository>?> {
        return try {
            when (val result = githubApi.getRepos(login)) {
                is ApiResult.Success -> {
                    ServiceResult.Success(result.value.map { it.mapToRepository() })
                }

                is ApiResult.Failure -> result.mapToServiceResult()
            }
        } catch (e: Exception) {
            e.mapToServiceResult("repositories")
        }
    }

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

    private fun <T> Exception.mapToServiceResult(context: String): ServiceResult<T> {
        return when (this) {
            is java.net.UnknownHostException -> ServiceResult.Failure(reason = "No network connection")
            is KotlinNullPointerException -> ServiceResult.Failure(reason = "$context not found", throwable = this)
            else -> ServiceResult.Failure(reason = "Unknown failure", throwable = this)
        }
    }
}