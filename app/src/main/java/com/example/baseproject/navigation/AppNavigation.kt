package com.example.baseproject.navigation

import android.os.Bundle
import com.example.core.navigationComponent.BaseNavigator

interface AppNavigation : BaseNavigator {

    fun openSplashToLoginScreen(bundle: Bundle? = null)

    fun openLoginToHomeScreen(bundle: Bundle? = null)

    fun openLoginToSignupScreen(bundle: Bundle? = null)

    fun openSignupToHomeScreen(bundle: Bundle? = null)

    fun openProfileToLoginScreen(bundle: Bundle? = null)

    fun openEditProfileScreen(bundle: Bundle? = null)

    fun openChatScreen(bundle: Bundle? = null)

}