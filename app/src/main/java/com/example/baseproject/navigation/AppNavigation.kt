package com.example.baseproject.navigation

import android.os.Bundle
import com.example.core.navigationComponent.BaseNavigator

interface AppNavigation : BaseNavigator {

    fun openSplashToLoginScreen(bundle: Bundle? = null)

    fun openSplashToHomeScreen(bundle: Bundle? = null)

    fun openLoginToHomeScreen(bundle: Bundle? = null)

    fun openLoginToSignupScreen(bundle: Bundle? = null)
}