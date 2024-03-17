package com.kuba.example.service.api

/**
 * A service interface for GitHub
 */
interface GithubService {

    /**
     * Search all github repositories
     *
     * @param query The search query
     */
    suspend fun searchRepos(query: String): ServiceResult<List<Repository>>

    /**
     * Retrieve contributes for a given [repository id][Repository.id]
     *
     * @param login The github login handle of the repository owner
     * @param repositoryName the name of the repository
     */
    suspend fun getContributors(
        login: String,
        repositoryName: String
    ): ServiceResult<List<User>?>

    /**
     * Retrieves user information
     *
     * @param login the user github handle
     */
    suspend fun getUserDetails(login: String): ServiceResult<User>

    /**
     * Retrieves repositories owned by a user
     *
     * @param login the user github handle
     */
    suspend fun getUserRepos(login: String): ServiceResult<List<Repository>?>
}

/**
 * Outcome of a service call
 */
sealed interface ServiceResult<T> {
    data class Success<T>(val value: T) : ServiceResult<T>
    data class Failure<T>(val reason: String?, val throwable: Throwable? = null) : ServiceResult<T>
}