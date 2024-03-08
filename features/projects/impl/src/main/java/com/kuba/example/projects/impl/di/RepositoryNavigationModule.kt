package com.kuba.example.projects.impl.di

import com.kuba.example.navigation.api.ControllerBuilder
import com.kuba.example.navigation.api.ControllerDestinationKey
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.api.navigation.RepositorySearchScreen
import com.kuba.example.projects.impl.contributors.ContributorsController
import com.kuba.example.projects.impl.search.RepositorySearchController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
object RepositoryNavigationModule {

    @Provides
    @IntoMap
    @ControllerDestinationKey(RepositorySearchScreen::class)
    fun provideRepositorySearchControllerBuilder(): ControllerBuilder =
        ControllerBuilder { destination -> RepositorySearchController() }

    @Provides
    @IntoMap
    @ControllerDestinationKey(ContributorsScreen::class)
    fun provideContributorsControllerBuilder(): ControllerBuilder =
        ControllerBuilder { destination -> ContributorsController(destination.args) }

}