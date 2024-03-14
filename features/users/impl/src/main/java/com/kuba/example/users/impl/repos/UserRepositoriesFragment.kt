package com.kuba.example.users.impl.repos

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.gson.Gson
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.api.navigation.RepositorySearchScreen
import com.kuba.example.users.api.navigation.UserDetailsScreen
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
import com.kuba.example.users.impl.R
import com.kuba.example.users.impl.databinding.ControllerUserRepositoriesBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class UserRepositoriesFragment : Fragment(R.layout.controller_user_repositories) {

    private val viewModel: UserRepositoriesViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.coroutineScope.launch {
            viewModel.loadRepositories()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ControllerUserRepositoriesBinding.bind(view)

        with(binding) {
            // Recyclerview adapter setup
            val adapter = GroupAdapter<GroupieViewHolder>()
            val repositorySection = Section()
            adapter.add(repositorySection)
            rvRepositories.adapter = adapter

            val user = UserRepositoriesScreen.extractUser(requireArguments())
            user.avatarUrl?.let { url ->
                imgAvatar.load(url) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }

            lblUserName.apply {
                text = user.name ?: user.login
                setOnClickListener {
                    val destination = UserDetailsScreen(
                        userLogin = user.login
                    )
                    val argument = UserDetailsScreen.extractUserLogin(destination.args)
                    findNavController().navigate("${destination.route}/$argument")
                }
            }

            // Observe viewmodel state emissions
            viewLifecycleOwner.lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    val itemClickListener: (repoOwnerLogin: String, repoName: String, repoDescription: String?) -> Unit =
                        { repoOwnerLogin, repoName, repoDescription ->
                            Timber.d("Navigate to contributors... $repoOwnerLogin/$repoName")
                            val destination = ContributorsScreen(
                                ownerLogin = repoOwnerLogin,
                                repoName = repoName,
                                repoDescription = repoDescription
                            )
                            val arguments = ContributorsScreen.extractArgs(destination.args)
                            val encodedArguments = Uri.encode(Gson().toJson(arguments))
                            findNavController().navigate(
                                route = "${destination.route}/$encodedArguments",
                                navOptions = NavOptions.Builder()
                                    .setPopUpTo(RepositorySearchScreen.ROUTE, false)
                                    .build()
                            )
                        }
                    viewModel.state.collectLatest { uiModel ->

                        when (uiModel) {
                            is UserRepositoriesUiModel.Content -> {
                                lblError.isVisible = false
                                val items = uiModel.repositories.map {
                                    RepositoryItem(
                                        owner = it.ownerLogin,
                                        name = it.name,
                                        description = it.description,
                                        stars = it.stars
                                    ).apply {
                                        onClickListener = itemClickListener
                                    }
                                }
                                repositorySection.update(items)
                            }

                            is UserRepositoriesUiModel.Error -> {
                                lblError.isVisible = true
                                lblError.text = uiModel.message
                            }
                        }
                    }
                }
            }

        }
    }
}
