package com.kuba.example.projects.api.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import com.kuba.example.navigation.api.ControllerDestination
import kotlinx.parcelize.Parcelize

class ContributorsScreen(ownerLogin: String, repoName: String, repoDescription: String?) : ControllerDestination {
    override val route: String = ROUTE
    override val args: Bundle = Bundle().apply {
        putParcelable(KEY_ARGS, ContributorScreenArgs(ownerLogin, repoName, repoDescription))
    }

    companion object {
        /**
         * Provide the [Bundle] set on the [Controller] to extract the [ContributorScreenArgs]
         */
        fun extractArgs(args: Bundle) : ContributorScreenArgs = args.getParcelable(KEY_ARGS)!!

        fun extractArgs(savedStateHandle: SavedStateHandle) = savedStateHandle.get<ContributorScreenArgs>(KEY_ARGS)!!

        const val KEY_ARGS = "key.args"
        const val ROUTE = "repos/contributors"
    }

    @Parcelize
    data class ContributorScreenArgs(
        val login: String,
        val repoName: String,
        val repoDescription: String?
    ) : Parcelable
}
