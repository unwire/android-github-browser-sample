package com.kuba.example.dagger.conductor.scope

import javax.inject.Scope
import kotlin.reflect.KClass

/**
 * Dagger scope defined by the class input as value
 *
 *
 * Intended to be used to declare scopes for [com.bluelinelabs.conductor.Controller].
 */
@Scope
annotation class ControllerScope(val value: KClass<*>)