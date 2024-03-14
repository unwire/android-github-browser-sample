package com.kuba.example.users.impl.repos

import android.os.Bundle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.fragment.fragment
import com.google.gson.Gson
import com.kuba.example.service.api.User
import com.kuba.example.users.api.navigation.UserRepositoriesScreen

fun NavGraphBuilder.userRepositoryDestination() {
    fragment<UserRepositoriesFragment>("${UserRepositoriesScreen.ROUTE}/{${UserRepositoriesScreen.KEY_USER}}") {
        argument(UserRepositoriesScreen.KEY_USER) {
            type = userRepositoriesType
        }
    }
}

private val userRepositoriesType = object : NavType<User>(
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
