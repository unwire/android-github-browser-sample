package com.kuba.example.projects.impl.contributors

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.impl.R
import com.kuba.example.projects.impl.databinding.ControllerContributorsBinding
import com.kuba.example.projects.impl.search.renderError
import com.kuba.example.projects.impl.search.setupAdapter
import com.kuba.example.service.api.User
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ContributorsFragment : Fragment(R.layout.controller_contributors) {
    private var _binding : ControllerContributorsBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.
    private val viewModel: ContributorsFragmentViewModel by viewModels()
    private val repositorySection by lazy { Section() }
    private val itemClickListener = { user: User -> navigateToUserRepositories(user) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ControllerContributorsBinding.bind(view)
        val args = requireArguments()
        val contributorScreenArgs = ContributorsScreen.extractArgs(args)
        with(binding) {
            with(contributorScreenArgs) {
                lblName.text = repoName
                lblDescription.text = repoDescription
            }
            rvContributors.setupAdapter(repositorySection)

            viewLifecycleOwner.lifecycleScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    observeContributors()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.coroutineScope.launch { viewModel.loadContributors() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private suspend fun observeContributors(){
        viewModel.state.collectLatest { uiModel ->
            when (uiModel) {
                is ContributorsUiModel.Content -> {
                    binding.rvContributors.isVisible = true
                    binding.lblError.isVisible = false
                    val items = uiModel.repositories.map { user ->
                        UserItem(user).apply { onClickListener = itemClickListener }
                    }
                    repositorySection.update(items) // TODO: Handle empty state
                }
                is ContributorsUiModel.Error -> {
                    binding.lblError.renderError(uiModel.message)
                    binding.rvContributors.isVisible = false
                }
                ContributorsUiModel.Loading -> {  }// TODO: Add Loading state
                ContributorsUiModel.Idle -> { /* no-op */ }
            }
        }
    }

    private fun navigateToUserRepositories(user: User) {
        Timber.d("Navigate to repository for user (login)... ${user.login}")
    }
}