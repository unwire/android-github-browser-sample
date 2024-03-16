package com.kuba.example.users.api.navigation

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import com.kuba.example.navigation.api.ControllerDestination
import com.kuba.example.service.api.User
import kotlinx.serialization.json.Json

class UserRepositoriesScreen(user: User) : ControllerDestination {

    override val route: String = USER_REPOSITORIES_ROUTE
    override val args: Bundle = Bundle().apply {
        putParcelable(KEY_USER, user)
    }

    companion object {
        /**
         * Provide the [Bundle] set on the [Controller] to extract the [User]
         */
        fun extractUser(args: Bundle) : User = args.getParcelable(KEY_USER)!!

        fun extractUserFromSavedStateHandle(savedStateHandle: SavedStateHandle) = savedStateHandle.get<User>(KEY_USER)!!

        // User login handle
        const val KEY_USER = "key.user"
        const val USER_REPOSITORIES_ROUTE = "user/repos"
        val USER_NAV_TYPE = object : NavType<User>(
            isNullableAllowed = false,
        ) {
            override fun parseValue(value: String): User = Json.decodeFromString(value)
            override fun put(bundle: Bundle, key: String, value: User) = bundle.putParcelable(key, value)
            override fun get(bundle: Bundle, key: String): User = bundle.getParcelable(key)!!
        }
    }
}