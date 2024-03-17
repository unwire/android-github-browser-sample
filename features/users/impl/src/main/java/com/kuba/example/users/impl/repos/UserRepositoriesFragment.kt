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
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.users.impl.R
import com.kuba.example.users.impl.databinding.ControllerUserRepositoriesBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

@AndroidEntryPoint
class UserRepositoriesFragment : Fragment(R.layout.controller_user_repositories){
    private var _binding : ControllerUserRepositoriesBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.
    private val viewModel: UserRepositoriesFragmentViewModel by viewModels()
    private val repositorySection by lazy { Section() }
    private val itemClickListener = { contributors: ContributorsScreen -> navigateToContributors(contributors) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ControllerUserRepositoriesBinding.bind(view)

        with(binding) {
            val adapter = GroupAdapter<GroupieViewHolder>()
            adapter.add(repositorySection)
            rvRepositories.adapter = adapter

            val user = viewModel.user
            user.avatarUrl?.let { url ->
                imgAvatar.load(url) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }
            lblUserName.text = user.name ?: user.login
            viewLifecycleOwner.lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    observeUserRepositories()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.coroutineScope.launch {
            viewModel.loadRepositories()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun observeUserRepositories(){
        viewModel.state.collectLatest { uiModel ->
            when (uiModel) {
                is UserRepositoriesUiModel.Content -> {
                    binding.indeterminateBar.isVisible = false
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

                    if (items.isEmpty()) {
                        binding.rvRepositories.isVisible = false
                        binding.lblError.text = (getString(R.string.no_repositories_found))
                        binding.lblError.isVisible = true
                    } else {
                        binding.lblError.isVisible = false
                        repositorySection.update(items)
                        binding.rvRepositories.isVisible = true
                    }
                }
                is UserRepositoriesUiModel.Error -> {
                    binding.rvRepositories.isVisible = false
                    binding.indeterminateBar.isVisible = false
                    with(binding.lblError) {
                        isVisible = true
                        text = uiModel.message
                    }
                }
                UserRepositoriesUiModel.Loading -> {
                    binding.indeterminateBar.isVisible = true
                    binding.lblError.isVisible = false
                    binding.rvRepositories.isVisible = false
                }
                UserRepositoriesUiModel.Idle -> { /* no-op */ }
            }
        }
    }

    private fun navigateToContributors(contributors: ContributorsScreen) {
        ContributorsScreen.extractArgs(contributors.args).let {
            Timber.d("Navigate to contributors... ${it.login}/${it.repoName}")
            val argument = Uri.encode(Json.encodeToString(it))
            findNavController().navigate("${contributors.route}/$argument")
        }
    }
}