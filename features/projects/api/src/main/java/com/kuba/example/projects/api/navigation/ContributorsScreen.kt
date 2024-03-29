package com.kuba.example.projects.api.navigation

import android.os.Bundle
import android.os.Parcelable
import com.kuba.example.navigation.api.ControllerDestination
import kotlinx.parcelize.Parcelize

class ContributorsScreen(ownerLogin: String, repoName: String, repoDescription: String?) : ControllerDestination {
    override val route: String = "repos/contributors"
    override val args: Bundle = Bundle().apply {
        putParcelable(KEY_ARGS, ContributorScreenArgs(ownerLogin, repoName, repoDescription))
    }

    companion object {
        /**
         * Provide the [Bundle] set on the [Controller] to extract the [ContributorScreenArgs]
         */
        fun extractArgs(args: Bundle) : ContributorScreenArgs = args.getParcelable(KEY_ARGS)!!

        private const val KEY_ARGS = "key.args"
    }

    @Parcelize
    data class ContributorScreenArgs(
        val login: String,
        val repoName: String,
        val repoDescription: String?
    ) : Parcelable
}