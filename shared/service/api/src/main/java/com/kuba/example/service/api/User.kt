package com.kuba.example.service.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class User(
    /**
     * User handle
     */
    val login: String,
    val name: String?,
    val avatarUrl: String?,
    val bio: String? = null,
    val followers: Int = 0,
    val following: Int = 0
) : Parcelable
