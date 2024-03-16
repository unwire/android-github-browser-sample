package com.kuba.example.service.impl.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDTO(
    val login: String,
    @Json(name = "avatar_url")
    val avatarUrl: String?,
    val name: String?,
    val bio: String?,
    val followers: Int,
    val following: Int
)
