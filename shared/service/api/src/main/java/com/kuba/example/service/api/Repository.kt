package com.kuba.example.service.api

data class Repository(
    val id: Int,

    val name: String,

    val description: String?,

    /**
     * Equivalent [User.login] (handle)
     */
    val ownerLogin: String,

    val stars: Int
)
