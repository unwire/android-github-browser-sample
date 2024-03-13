package com.kuba.example.users.api.navigation

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.kuba.example.navigation.api.ControllerDestination
import com.kuba.example.service.api.User

class UserRepositoriesScreen(user: User) : ControllerDestination {

    override val route: String = "user/repos"
    override val args: Bundle = Bundle().apply {
        putParcelable(KEY_USER, user)
    }

    companion object {
        /**
         * Provide the [Bundle] set on the [Controller] to extract the [User]
         */
        fun extractUser(args: Bundle) : User = args.getParcelable(KEY_USER)!!

        fun extractUser(savedStateHandle: SavedStateHandle) : User = savedStateHandle.get<User>(KEY_USER)!!

        // User login handle
        private const val KEY_USER = "key.user"
    }

}
