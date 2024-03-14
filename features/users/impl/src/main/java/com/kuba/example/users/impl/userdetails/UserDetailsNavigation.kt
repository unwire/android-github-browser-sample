package com.kuba.example.users.impl.userdetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.fragment.fragment
import com.kuba.example.users.api.navigation.UserDetailsScreen

fun NavGraphBuilder.userDetailsDestination() {
    fragment<UserDetailsFragment>("${UserDetailsScreen.ROUTE}/{${UserDetailsScreen.KEY_USER_LOGIN}}") {
        argument(UserDetailsScreen.KEY_USER_LOGIN) {
            type = NavType.StringType
        }
    }
}
