package com.kuba.example.service.impl.data.api

import com.kuba.example.service.impl.data.api.dto.ContributorDTO
import com.kuba.example.service.impl.data.api.dto.RepoDTO
import com.kuba.example.service.impl.data.api.dto.RepoSearchResponseDTO
import com.kuba.example.service.impl.data.api.dto.UserDTO
import com.slack.eithernet.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Github REST api
 */
interface GithubApi {

    @GET("users/{login}")
    fun getUser(@Path("login") login: String): ApiResult<UserDTO, Unit>

    @GET("users/{login}/repos")
    fun getRepos(@Path("login") login: String): ApiResult<List<RepoDTO>, Unit>

    @GET("repos/{owner}/{name}")
    fun getRepo(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): ApiResult<RepoDTO, Unit>

    @GET("repos/{owner}/{name}/contributors")
    fun getContributors(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): ApiResult<List<ContributorDTO>, Unit>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String): ApiResult<RepoSearchResponseDTO, Unit>

}