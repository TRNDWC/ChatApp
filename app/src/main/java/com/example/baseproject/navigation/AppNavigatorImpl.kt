package com.example.baseproject.navigation

import android.os.Bundle
import com.example.baseproject.R
import com.example.core.navigationComponent.BaseNavigatorImpl
import com.example.setting.DemoNavigation
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AppNavigatorImpl @Inject constructor() : BaseNavigatorImpl(),
    AppNavigation, DemoNavigation {

    override fun openSplashToLoginScreen(bundle: Bundle?) {
        openScreen(R.id.action_splashFragment_to_loginFragment, bundle)
    }

    override fun openLoginToHomeScreen(bundle: Bundle?) {
        openScreen(R.id.action_loginFragment_to_homeFragment, bundle)
    }

    override fun openLoginToSignupScreen(bundle: Bundle?) {
        openScreen(R.id.action_loginFragment_to_signupFragment, bundle)
    }

    override fun openSignupToHomeScreen(bundle: Bundle?) {
        openScreen(R.id.action_signupFragment_to_homeFragment, bundle)
    }

    override fun openProfileToLoginScreen(bundle: Bundle?) {
        openScreen(R.id.action_homeFragment_to_loginFragment, bundle)
    }

    override fun openEditProfileScreen(bundle: Bundle?) {
        openScreen(R.id.action_homeFragment_to_editProfileFragment, bundle)
    }

    override fun openChatScreen(bundle: Bundle?) {
        openScreen(R.id.action_homeFragment_to_chatFragment, bundle)
    }


}