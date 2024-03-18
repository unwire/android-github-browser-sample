package com.kuba.example.service.impl.data.db

import com.kuba.example.service.api.Repository

fun RepositoryItem.mapToRepository(): Repository = Repository(
    id = id,
    name = name,
    description = description,
    ownerLogin = ownerLogin,
    stars = stars
)

fun Repository.mapToRepositoryItem(): RepositoryItem = RepositoryItem(
    id = id,
    name = name,
    description = description,
    ownerLogin = ownerLogin,
    stars = stars
)

fun List<RepositoryItem>.mapToRepository(): List<Repository> = map { it.mapToRepository() }
