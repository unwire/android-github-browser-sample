package com.kuba.example.service.impl.data.db

import androidx.room.Entity

@Entity(tableName = "repositories", primaryKeys = ["id"])
data class RepositoryItem(
    val id: Int,
    val name: String,
    val description: String?,
    val ownerLogin: String,
    val stars: Int
)
