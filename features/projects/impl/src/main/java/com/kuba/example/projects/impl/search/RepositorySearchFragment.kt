package com.kuba.example.projects.impl.search

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.impl.R
import com.kuba.example.projects.impl.databinding.ControllerRepositorySearchBinding
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

@AndroidEntryPoint
class RepositorySearchFragment : Fragment(R.layout.controller_repository_search){
    private val viewModel: RepositorySearchFragmentViewModel by viewModels()
    private var _binding : ControllerRepositorySearchBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.
    private val itemClickListener = { destination: ContributorsScreen -> navigateToContributors(destination) }
    private val mapRepositoryToRepositoryItem by lazy { MapRepositoryToRepositoryItem(itemClickListener) }
    private val repositorySection by lazy { Section() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ControllerRepositorySearchBinding.bind(view)
        with(binding) {
            rvRepositories.setupAdapter(repositorySection)
            btnSearch.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val query: String = editQuery.text.toString()
                    viewModel.search(query)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                observeRepositories()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private suspend fun observeRepositories(){
        viewModel.state.collectLatest { uiModel ->
            when (uiModel) {
                is RepositoriesUiModel.Content -> {
                    binding.indeterminateBar.isVisible = false
                    binding.lblError.isVisible = false
                    binding.rvRepositories.isVisible = true
                    val items = mapRepositoryToRepositoryItem.invoke(uiModel.repositories)
                    repositorySection.update(items) // TODO: Handle empty state
                }
                is RepositoriesUiModel.Error -> {
                    binding.rvRepositories.isVisible = false
                    binding.indeterminateBar.isVisible = false
                    binding.lblError.renderError(uiModel.message)
                }
                RepositoriesUiModel.Loading -> {
                    binding.indeterminateBar.isVisible = true
                    binding.lblError.isVisible = false
                    binding.rvRepositories.isVisible = false
                }
                RepositoriesUiModel.Idle -> { /* no-op */ }
            }
        }
    }

    private fun navigateToContributors(destination: ContributorsScreen) {
        ContributorsScreen.extractArgs(destination.args).let {
            Timber.d("Navigate to contributors... ${it.login}/${it.repoName}")
            val argument = Uri.encode(Json.encodeToString(it))
            findNavController().navigate("${destination.route}/$argument")
        }
    }
}