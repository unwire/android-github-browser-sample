package com.kuba.example.service.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetails(
    val login: String,
    val name: String?,
    val email: String?,
    val followers: Int,
    val following: Int,
    val location: String?,
    val avatarUrl: String?
) : Parcelable
