package com.kuba.example.navigation.api

import com.bluelinelabs.conductor.Controller


/**
 * Any [Controller] based screen must have a [ControllerBuilder] implementation that is injected
 * into a dagger-multibound map, eg:
 *
 * ```
 *     @Provides
 *     @IntoMap
 *     @ControllerDestinationKey(RepositorySearchScreen::class)
 *     fun provideRepositorySearchControllerBuilder(): ControllerBuilder =
 *         ControllerBuilder { destination -> RepositorySearchController() }
 * ```
 */
fun interface ControllerBuilder {
    fun build(destination: ControllerDestination): Controller
}



