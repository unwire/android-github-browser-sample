package com.kuba.example.dagger.conductor

import com.bluelinelabs.conductor.Controller
import com.kuba.example.dagger.conductor.HasControllerInjector

/**
 * Legacy.
 *
 * Implementation (usually an [android.app.Activity]) provides a HasControllerInjector, which can
 * provide components for [Controller]s to inject themselves into.
 *
 * This provider-abstraction around [HasControllerInjector] is necessary for the traversal
 * of the Controller->Router->Activity->Application hierarchy inside [ConductorInjection] and
 * essentially because one cannot create (easily) subclasses of [com.bluelinelabs.conductor.Router]
 * that implement [HasControllerInjector].
 */
interface HasControllerInjectorProvider {
    fun controllerInjector(controller: Controller): HasControllerInjector
}