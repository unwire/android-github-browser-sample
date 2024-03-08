package com.kuba.example.navigation

import com.bluelinelabs.conductor.Controller
import com.kuba.example.navigation.api.ControllerBuilder
import com.kuba.example.navigation.api.ControllerDestination
import com.kuba.example.navigation.api.ControllerFactory
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Contains a map of the entire set of controller destinations in an application (thus, the singleton).
 */
@Singleton
class MultiBoundControllerFactory @Inject constructor(
    private val controllerBuilders: Map<Class<out ControllerDestination>, @JvmSuppressWildcards ControllerBuilder>
) : ControllerFactory {

    /**
     * Resolves a [Controller] from a given [ControllerDestination] or throws if the [ControllerDestination]
     * has not been provided by any module.
     */
    override fun  <T : ControllerDestination> create(controllerDestination: T) : Controller {
        val builder = controllerBuilders[controllerDestination::class.java]
            ?: throw IllegalArgumentException("Could not find destination with route: ${controllerDestination.route}")

        return builder.build(controllerDestination)
    }

}