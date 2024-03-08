package com.kuba.example.users.impl.di

import com.kuba.example.navigation.api.ControllerBuilder
import com.kuba.example.navigation.api.ControllerDestinationKey
import com.kuba.example.users.api.navigation.UserRepositoriesScreen
import com.kuba.example.users.impl.repos.UserRepositoriesController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
object UserNavigationModule {

    @Provides
    @IntoMap
    @ControllerDestinationKey(UserRepositoriesScreen::class)
    fun provideRepositorySearchControllerBuilder(): ControllerBuilder =
        ControllerBuilder { destination -> UserRepositoriesController(destination.args) }

}