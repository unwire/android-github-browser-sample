package com.kuba.example.users.api.navigation

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.kuba.example.navigation.api.ControllerDestination

class UserDetailsScreen(userLogin: String) : ControllerDestination {

    override val route: String = ROUTE
    override val args: Bundle = Bundle().apply {
        putString(KEY_USER_LOGIN, userLogin)
    }

    companion object {
        fun extractUserLogin(bundle: Bundle): String = bundle.getString(KEY_USER_LOGIN)!!
        fun extractUserLogin(savedStateHandle: SavedStateHandle): String =
            savedStateHandle.get<String>(KEY_USER_LOGIN)!!

        // User login handle
        const val KEY_USER_LOGIN = "key.user_login"
        const val ROUTE = "user/details"
    }

}
