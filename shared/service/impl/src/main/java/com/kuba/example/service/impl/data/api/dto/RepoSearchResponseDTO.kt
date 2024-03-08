package com.kuba.example.service.impl.data.api.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RepoSearchResponseDTO(
    @Json(name = "total_count")
    val total: Int = 0,

    val items: List<RepoDTO>
)