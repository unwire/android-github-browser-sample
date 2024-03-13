package com.kuba.example.projects.impl.contributors

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
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.impl.R
import com.kuba.example.projects.impl.databinding.ControllerContributorsBinding
import com.kuba.example.service.api.User
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ContributorsFragment : Fragment(R.layout.controller_contributors) {

    private val viewModel: ContributorsViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.coroutineScope.launch {
            viewModel.loadContributors()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ControllerContributorsBinding.bind(view)
        with(binding) {
            with(ContributorsScreen.extractArgs(requireArguments())) {
                lblName.text = repoName
                lblDescription.text = repoDescription
            }

            // Recyclerview adapter setup
            val adapter = GroupAdapter<GroupieViewHolder>()
            val repositorySection = Section()
            adapter.add(repositorySection)
            rvContributors.adapter = adapter

            // Observe viewmodel state emissions
            viewLifecycleOwner.lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    val itemClickListener: (user: User) -> Unit =
                        { user ->
                            Timber.d("Navigate to repository for user (login)... ${user.login}")
                            val destination = UserRepositoriesScreen(user)
                            val arguments = UserRepositoriesScreen.extractUser(destination.args)
                            val encodedArguments = Uri.encode(Gson().toJson(arguments))
                            findNavController().navigate("${destination.route}/$encodedArguments")
                        }
                    viewModel.state.collectLatest { uiModel ->

                        when (uiModel) {
                            is ContributorsUiModel.Content -> {
                                lblError.isVisible = false
                                val items = uiModel.repositories.map { user ->
                                    UserItem(user).apply {
                                        onClickListener = itemClickListener
                                    }
                                }
                                repositorySection.update(items)
                            }

                            is ContributorsUiModel.Error -> {
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
