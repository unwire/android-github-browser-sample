package com.kuba.example.navigation.api

import com.bluelinelabs.conductor.Controller

/**
 * Creates real [Controller]s from [ControllerDestination] specs
 */
interface ControllerFactory {

    fun  <T : ControllerDestination> create(controllerDestination: T) : Controller
}