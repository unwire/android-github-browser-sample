package com.kuba.example.navigation

import com.kuba.example.navigation.api.FeatureFlagFactory
import javax.inject.Inject

class FeatureFlagFactoryImpl @Inject constructor()  : FeatureFlagFactory {
    override fun isAndroidNavigationEnabled(): Boolean = false
}