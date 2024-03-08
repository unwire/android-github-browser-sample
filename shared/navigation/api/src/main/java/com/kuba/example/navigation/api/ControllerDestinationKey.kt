package com.kuba.example.navigation.api

import dagger.MapKey
import dagger.multibindings.ClassKey
import kotlin.reflect.KClass

/**
 * The dagger multi-bound map key for a controller destination
 *
 * NOTE: Cannot use [ClassKey] type due to: https://github.com/google/dagger/issues/1478
 */
@MapKey
annotation class ControllerDestinationKey(val value: KClass<out ControllerDestination>)