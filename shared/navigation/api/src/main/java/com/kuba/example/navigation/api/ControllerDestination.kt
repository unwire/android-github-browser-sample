package com.kuba.example.navigation.api

import android.os.Bundle

/**
 * A "spec" for a [Controller] destination. Subclasses exposed in :api modules
 */
interface ControllerDestination {
    val route: String

    val args: Bundle?
}