package com.kuba.example.projects.impl.contributors

import android.content.Context
import android.os.Bundle
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
import com.kuba.example.projects.impl.databinding.ControllerContributorsBinding
import com.kuba.example.service.api.User
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
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

@Deprecated("Will be deleted, Use ContributorsFragment instead")
class ContributorsController(args: Bundle?) :
    BaseController(R.layout.controller_contributors, args) {

    @Inject
    lateinit var controllerFactory: ControllerFactory

    @Inject
    lateinit var viewModel: ContributorsViewModel

    override fun onContextAvailable(context: Context) {
        if (!::viewModel.isInitialized) { // prevent multiple injections on ie. configuration changes
            ConductorInjection.inject(this)
        }
    }

    override fun onAttach(view: View) {
        lifecycle.coroutineScope.launch {
            viewModel.loadContributors()
        }
    }

    override fun onViewCreated(view: View) {
        val binding = ControllerContributorsBinding.bind(view)
        with(binding) {
            with(ContributorsScreen.extractArgs(args)) {
                lblName.text = repoName
                lblDescription.text = repoDescription
            }

            // Recyclerview adapter setup
            val adapter = GroupAdapter<GroupieViewHolder>()
            val repositorySection = Section()
            adapter.add(repositorySection)
            rvContributors.adapter = adapter

            // Observe viewmodel state emissions
            lifecycle.coroutineScope.launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    val itemClickListener: (user: User) -> Unit =
                        { user ->
                            Timber.d("Navigate to repository for user (login)... ${user.login}")
                            val destination = UserRepositoriesScreen(user)
                            router.pushController(
                                RouterTransaction
                                        .with(controllerFactory.create(destination))
                                    .pushChangeHandler(HorizontalChangeHandler())
                                    .popChangeHandler(HorizontalChangeHandler())
                            )
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
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    @ControllerScope(ContributorsController::class)
    @Subcomponent(modules = [ViewModelModule::class])
    interface Component : AndroidInjector<ContributorsController> {
        @Subcomponent.Factory
        abstract class Factory : AndroidInjector.Factory<ContributorsController>
    }

    @InstallIn(ActivityComponent::class)
    @Module(subcomponents = [Component::class])
    abstract class BindingModule {
        @Binds
        @IntoMap
        @ClassKey(ContributorsController::class)
        abstract fun controllerComponentFactory(
            impl: Component.Factory
        ): AndroidInjector.Factory<*>
    }

    @DisableInstallInCheck
    @Module
    object ViewModelModule {
        @Provides
        fun providerScreenArgs(controller: ContributorsController): ContributorsScreen.ContributorScreenArgs =
            ContributorsScreen.extractArgs(requireNotNull(controller.args))
    }
}