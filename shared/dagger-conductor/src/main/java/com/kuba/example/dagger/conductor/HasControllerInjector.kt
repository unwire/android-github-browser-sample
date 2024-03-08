package com.kuba.example.dagger.conductor

import com.bluelinelabs.conductor.Controller
import dagger.android.DispatchingAndroidInjector

/**
 * Legacy.
 *
 * Implementations of this interface can provide a [DispatchingAndroidInjector] of [Controller]s.
 * Meaning it knows how to deliver components for Controllers to inject themselves (member/field injection)
 */
interface HasControllerInjector {
    fun controllerInjector(): DispatchingAndroidInjector<Controller>
}