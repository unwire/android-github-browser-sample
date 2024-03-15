package com.kuba.example.navigation.api

interface FeatureFlagFactory {
    fun isAndroidNavigationEnabled(): Boolean
}