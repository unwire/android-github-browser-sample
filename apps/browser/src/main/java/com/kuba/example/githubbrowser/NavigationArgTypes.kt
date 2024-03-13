package com.kuba.example.githubbrowser

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.service.api.User

val contributorScreenArgsType =
    object : NavType<ContributorsScreen.ContributorScreenArgs>(
        isNullableAllowed = false
    ) {
        override fun put(
            bundle: Bundle,
            key: String,
            value: ContributorsScreen.ContributorScreenArgs
        ) {
            bundle.putParcelable(key, value)
        }

        override fun get(
            bundle: Bundle,
            key: String
        ): ContributorsScreen.ContributorScreenArgs? {
            return bundle.getParcelable(key) as? ContributorsScreen.ContributorScreenArgs
        }

        override fun parseValue(value: String): ContributorsScreen.ContributorScreenArgs {
            return Gson().fromJson(value, ContributorsScreen.ContributorScreenArgs::class.java)
        }
    }

val userRepositoriesType = object : NavType<User>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: User) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): User? {
        return bundle.getParcelable(key) as? User
    }

    override fun parseValue(value: String): User {
        return Gson().fromJson(value, User::class.java)
    }
}
