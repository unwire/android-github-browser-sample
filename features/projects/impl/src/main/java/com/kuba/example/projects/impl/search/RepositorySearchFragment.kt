package com.kuba.example.projects.impl.search

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
import com.kuba.example.projects.impl.databinding.ControllerRepositorySearchBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RepositorySearchFragment : Fragment(R.layout.controller_repository_search) {

    private val viewModel: RepositorySearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ControllerRepositorySearchBinding.bind(view)
        with(binding) {
            // Recyclerview adapter setup
            val adapter = GroupAdapter<GroupieViewHolder>()
            val repositorySection = Section()
            adapter.add(repositorySection)
            rvRepositories.adapter = adapter


            // Observe viewmodel state emissions
            viewLifecycleOwner.lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    val itemClickListener: (repoOwnerLogin: String, repoName: String, repoDescription: String?) -> Unit =
                        { repoOwnerLogin, repoName, repoDescription ->
                            editQuery.clearFocus() // dismiss keyboard
                            Timber.d("Navigate to contributors... $repoOwnerLogin/$repoName")
                            val destination = ContributorsScreen(
                                ownerLogin = repoOwnerLogin,
                                repoName = repoName,
                                repoDescription = repoDescription
                            )
                            val arguments = ContributorsScreen.extractArgs(destination.args)
                            val encodedArguments = Uri.encode(Gson().toJson(arguments))
                            findNavController().navigate("${destination.route}/$encodedArguments")
                        }
                    viewModel.state.collectLatest { uiModel ->

                        when (uiModel) {
                            is RepositoriesUiModel.Content -> {
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

                            is RepositoriesUiModel.Error -> {
                                lblError.isVisible = true
                                lblError.text = uiModel.message
                            }
                        }
                    }
                }
            }

            btnSearch.setOnClickListener {
                lifecycle.coroutineScope.launch {
                    val query: String = editQuery.text.toString()
                    viewModel.search(query)
                }
            }
        }
    }
}
