package com.kuba.example.dagger.conductor

import com.bluelinelabs.conductor.Controller
import dagger.Module
import dagger.android.AndroidInjector
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.Multibinds

/**
 * Legacy.
 *
 * A declaration of the multibound map type, which is required by dagger if the map is empty
 * (no [IntoMap] annotations)
 */
@InstallIn(ActivityComponent::class)
@Module
abstract class ConductorInjectionModule private constructor() {

    @Multibinds
    abstract fun controllerInjectorFactories(): Map<Class<out Controller>, AndroidInjector.Factory<*>>

    @Multibinds
    abstract fun controllerStringInjectorFactories(): Map<String, AndroidInjector.Factory<*>>

}