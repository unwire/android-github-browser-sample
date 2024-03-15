package com.kuba.example.navigation.di

import com.kuba.example.navigation.FeatureFlagFactoryImpl
import com.kuba.example.navigation.MultiBoundControllerFactory
import com.kuba.example.navigation.api.ControllerBuilder
import com.kuba.example.navigation.api.ControllerDestination
import com.kuba.example.navigation.api.ControllerFactory
import com.kuba.example.navigation.api.FeatureFlagFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds

@InstallIn(SingletonComponent::class)
@Module
interface NavModule {

    @Binds
    fun controllerDestinations(multiBoundControllerFactory: MultiBoundControllerFactory): ControllerFactory

    @ControllerBuilders
    @Multibinds
    fun controllerBuilders(): Map<Class<out ControllerDestination>, ControllerBuilder>

    @Binds
    fun featureFlagFactory(impl: FeatureFlagFactoryImpl): FeatureFlagFactory
}