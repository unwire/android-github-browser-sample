package com.kuba.example.service.impl.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    val login: String,
    val name: String?,
    val email: String?,
    val followers: Int,
    val following: Int,
    val location: String?,
    @Json(name = "avatar_url")
    val avatarUrl: String?
)
