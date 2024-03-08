package com.kuba.example.users.impl.repos

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import coil.transform.CircleCropTransformation
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.kuba.example.dagger.conductor.ConductorInjection
import com.kuba.example.dagger.conductor.scope.ControllerScope
import com.kuba.example.navigation.api.BaseController
import com.kuba.example.navigation.api.ControllerFactory
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.service.api.User
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
import com.kuba.example.users.impl.R
import com.kuba.example.users.impl.databinding.ControllerUserRepositoriesBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.migration.DisableInstallInCheck
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UserRepositoriesController(args: Bundle?) : BaseController(R.layout.controller_user_repositories, args) {

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewModel: UserRepositoriesViewModel

    override fun onContextAvailable(context: Context) {
        if (!::viewModel.isInitialized) { // prevent multiple injections on ie. configuration changes
            ConductorInjection.inject(this)
        }
    }

    override fun onAttach(view: View) {
        lifecycle.coroutineScope.launch {
            viewModel.loadRepositories()
        }
    }

    override fun onViewCreated(view: View) {
        super.onViewCreated(view)
        val binding = ControllerUserRepositoriesBinding.bind(view)

        with(binding) {
            // Recyclerview adapter setup
            val adapter = GroupAdapter<GroupieViewHolder>()
            val repositorySection = Section()
            adapter.add(repositorySection)
            rvRepositories.adapter = adapter

            val user = UserRepositoriesScreen.extractUser(args)
            user.avatarUrl?.let { url ->
                imgAvatar.load(url) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }

            lblUserName.text = user.name ?: user.login

            // Observe viewmodel state emissions
            lifecycle.coroutineScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    val itemClickListener: (repoOwnerLogin: String, repoName: String, repoDescription: String?) -> Unit =
                        { repoOwnerLogin, repoName, repoDescription ->
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

    @ControllerScope(UserRepositoriesController::class)
    @Subcomponent(modules = [ViewModelModule::class])
    interface Component : AndroidInjector<UserRepositoriesController> {
        @Subcomponent.Factory
        abstract class Factory : AndroidInjector.Factory<UserRepositoriesController>
    }

    @InstallIn(ActivityComponent::class)
    @Module(subcomponents = [Component::class])
    abstract class BindingModule {
        @Binds
        @IntoMap
        @ClassKey(UserRepositoriesController::class)
        abstract fun controllerComponentFactory(
            impl: Component.Factory
        ): AndroidInjector.Factory<*>
    }

    @DisableInstallInCheck
    @Module
    object ViewModelModule {
        @Provides
        fun providerScreenArgs(controller: UserRepositoriesController): User =
            UserRepositoriesScreen.extractUser(requireNotNull(controller.args))
    }
}