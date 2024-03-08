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
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
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

class RepositorySearchController : BaseController(R.layout.controller_repository_search) {

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewModel: RepositorySearchViewModel

    override fun onContextAvailable(context: Context) {
        if (!::viewModel.isInitialized) { // prevent multiple injections on ie. configuration changes
            ConductorInjection.inject(this)
        }
    }

    override fun onViewCreated(view: View) {
        val binding = ControllerRepositorySearchBinding.bind(view)
        with(binding) {
            // Recyclerview adapter setup
            val adapter = GroupAdapter<GroupieViewHolder>()
            val repositorySection = Section()
            adapter.add(repositorySection)
            rvRepositories.adapter = adapter


            // Observe viewmodel state emissions
            lifecycle.coroutineScope.launch {
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
                            router.pushController(
                                RouterTransaction
                                        .with(controllerFactory.create(destination))
                                        .pushChangeHandler(HorizontalChangeHandler())
                                        .popChangeHandler(HorizontalChangeHandler())
                            )
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