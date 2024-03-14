package com.kuba.example.projects.impl.contributors

import android.os.Bundle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.fragment.fragment
import com.google.gson.Gson
import com.kuba.example.projects.api.navigation.ContributorsScreen

fun NavGraphBuilder.contributorsDestination() {
    fragment<ContributorsFragment>("${ContributorsScreen.ROUTE}/{${ContributorsScreen.KEY_ARGS}}") {
        argument(ContributorsScreen.KEY_ARGS) {
            type = contributorScreenArgsType
        }
    }
}

private val contributorScreenArgsType =
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
