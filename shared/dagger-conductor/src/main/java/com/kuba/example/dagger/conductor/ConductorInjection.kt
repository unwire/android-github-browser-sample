package com.kuba.example.dagger.conductor

import android.content.Context
import com.bluelinelabs.conductor.Controller
import timber.log.Timber

/**
 * Legacy.
 *
 * Helper class for traversing parent controllers -> routers -> activity -> application to find an
 * implementation of [HasControllerInjector] and use that to inject dependencies.
 */
object ConductorInjection {

    @JvmStatic
    @JvmOverloads
    fun inject(
        controller: Controller?,
        fallbackContext: Context? = null
    ) {
        requireNotNull(controller) { "Must provide non-null controller" }
        val hasControllerInjector =
            findHasControllerInjector(controller, fallbackContext)
        Timber.d(
            "An injector for ${controller.javaClass.canonicalName} was found in ${hasControllerInjector.javaClass.canonicalName}"
        )
        val controllerInjector = hasControllerInjector.controllerInjector()
        controllerInjector.inject(controller)
    }

    private fun findHasControllerInjector(
        controller: Controller, fallbackContext: Context?
    ): HasControllerInjector {
        var parentController: Controller? = controller

        // traverse hierarchy bottom-up, starting at the parent controller (if any)
        do {
            if (parentController?.parentController
                        .also { parentController = it } == null
            ) {
                val rootRouter = controller.router
                val activity = rootRouter.activity

                // step 1: look for a "router-scoped" component providing an injector
                // If there is a separate "router-scope", the activity must implement a provider
                // of HasControllerInjector instance. This delegation is necessary
                // since we cannot create custom Router objects - and thus not make them
                // implement HasControllerInjector directly
                if (activity is HasControllerInjectorProvider) {
                    return (activity as HasControllerInjectorProvider).controllerInjector(controller)
                }

                // step 2: look for an "activity-scoped" component providing an injector
                if (activity is HasControllerInjector) {
                    return activity
                }

                // step 3: look for an "app-scoped" component providing an injector
                if (activity!!.application is HasControllerInjector) {
                    return activity.application as HasControllerInjector
                }
                throw IllegalArgumentException(
                    "No injector was found for ${controller.javaClass.canonicalName}"
                )
            }
            // step 0: any parent controller can define a component scope providing an injector
        } while (parentController !is HasControllerInjector)
        // return a controller providing component builders itself
        return parentController as HasControllerInjector
    }
}