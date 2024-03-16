package com.kuba.example.projects.impl.search

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.kuba.example.dagger.conductor.ConductorInjection
import com.kuba.example.dagger.conductor.scope.ControllerScope
import com.kuba.example.navigation.api.BaseController
import com.kuba.example.navigation.api.ControllerFactory
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.impl.R
import com.kuba.example.projects.impl.databinding.ControllerRepositorySearchBinding
import com.xwray.groupie.Section
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@Deprecated("Will be removed, use RepositorySearchFragment instead")
class RepositorySearchController : BaseController(R.layout.controller_repository_search) {

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewModel: RepositorySearchViewModel

    private lateinit var binding: ControllerRepositorySearchBinding
    private val itemClickListener = { destination: ContributorsScreen -> navigateToContributors(destination) }
    private val mapRepositoryToRepositoryItem by lazy { MapRepositoryToRepositoryItem(itemClickListener) }
    private val repositorySection by lazy { Section() }

    override fun onContextAvailable(context: Context) {
        if (!::viewModel.isInitialized) { // prevent multiple injections on ie. configuration changes
            ConductorInjection.inject(this)
        }
    }

    override fun onViewCreated(view: View) {
        binding = ControllerRepositorySearchBinding.bind(view)
        with(binding) {
            rvRepositories.setupAdapter(repositorySection)
            btnSearch.setOnClickListener {
                lifecycle.coroutineScope.launch {
                    val query: String = editQuery.text.toString()
                    viewModel.search(query)
                }
            }
        }

        // Observe viewmodel state emissions
        lifecycle.coroutineScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                observeRepositories()
            }
        }
    }

    private suspend fun observeRepositories(){
        viewModel.state.collectLatest { uiModel ->
            when (uiModel) {
                is RepositoriesUiModel.Content -> {
                    binding.lblError.isVisible = false
                    val items = mapRepositoryToRepositoryItem.invoke(uiModel.repositories)
                    repositorySection.update(items)
                }
                is RepositoriesUiModel.Error -> binding.lblError.renderError(uiModel.message)
                RepositoriesUiModel.Idle -> { /* no-op */ }
                RepositoriesUiModel.Loading -> {}
            }
        }
    }

    private fun navigateToContributors(destination: ContributorsScreen) {
        ContributorsScreen.extractArgs(destination.args).let {
            Timber.d("Navigate to contributors... ${it.login}/${it.repoName}")
        }
        binding.editQuery.clearFocus() // dismiss keyboard
        router.pushController(
            RouterTransaction
                .with(controllerFactory.create(destination))
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler())
        )
    }

    @ControllerScope(RepositorySearchController::class)
    @Subcomponent
    interface Component : AndroidInjector<RepositorySearchController> {
        @Subcomponent.Factory
        abstract class Factory : AndroidInjector.Factory<RepositorySearchController>
    }

    @InstallIn(ActivityComponent::class)
    @Module(subcomponents = [Component::class])
    abstract class BindingModule {
        @Binds
        @IntoMap
        @ClassKey(RepositorySearchController::class)
        abstract fun controllerComponentFactory(
            impl: Component.Factory
        ): AndroidInjector.Factory<*>
    }
}