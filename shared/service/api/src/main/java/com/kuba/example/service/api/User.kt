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

    val avatarUrl: String?
) : Parcelable
