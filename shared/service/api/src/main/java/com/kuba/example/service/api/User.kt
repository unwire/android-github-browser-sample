package com.kuba.example.service.api

data class User(
    /**
     * User handle
     */
    val login: String,

    val name: String?,

    val avatarUrl: String?
)
