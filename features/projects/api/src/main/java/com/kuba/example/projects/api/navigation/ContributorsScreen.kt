package com.kuba.example.projects.api.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.kuba.example.navigation.api.ControllerDestination
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class ContributorsScreen(ownerLogin: String, repoName: String, repoDescription: String?) : ControllerDestination {
    override val route: String = CONTRIBUTORS_ROUTE
    override val args: Bundle = Bundle().apply {
        putParcelable(KEY_ARGS, ContributorScreenArgs(ownerLogin, repoName, repoDescription))
    }

    companion object {
        /**
         * Provide the [Bundle] set on the [Controller] to extract the [ContributorScreenArgs]
         */
        fun extractArgs(args: Bundle) : ContributorScreenArgs = args.getParcelable(KEY_ARGS)!!
        const val KEY_ARGS = "key.args"
        const val CONTRIBUTORS_ROUTE = "repos/contributors"
        val CONTRIBUTOR_SCREEN_ARGS_TYPE = object : NavType<ContributorScreenArgs>(
            isNullableAllowed = false,
        ) {
            override fun parseValue(value: String): ContributorScreenArgs = Json.decodeFromString(value)
            override fun put(bundle: Bundle, key: String, value: ContributorScreenArgs) = bundle.putParcelable(key, value)
            override fun get(bundle: Bundle, key: String): ContributorScreenArgs = bundle.getParcelable(key)!!
        }
    }

    @Parcelize
    @Serializable
    data class ContributorScreenArgs(
        val login: String,
        val repoName: String,
        val repoDescription: String?
    ) : Parcelable
}