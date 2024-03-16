package com.kuba.example.users.api.navigation

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.kuba.example.navigation.api.ControllerDestination

class UserDetailsScreen(login: String) : ControllerDestination {
    override val route: String = USER_DETAILS_REPOSITORIES_ROUTE
    override val args: Bundle = Bundle().apply {
        putString(KEY_USER, login)
    }

    companion object {
        fun extractArgs(args: Bundle) : String = args.getString(KEY_USER)!!

        fun extractSavedStateHandle(savedStateHandle: SavedStateHandle) = savedStateHandle.get<String>(KEY_USER)!!

        const val KEY_USER = "key.user"
        const val USER_DETAILS_REPOSITORIES_ROUTE = "user/details"
    }
}