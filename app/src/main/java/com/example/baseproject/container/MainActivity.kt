package com.example.baseproject.container

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.baseproject.R
import com.example.baseproject.databinding.ActivityMainBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.utils.LanguageConfig
import com.example.baseproject.utils.SharedPrefs
import com.example.core.base.BaseActivityNotRequireViewModel
import com.example.core.pref.RxPreferences
import com.example.core.utils.NetworkConnectionManager
import com.example.core.utils.setLanguage
import com.example.core.utils.toast
import com.example.setting.ui.home.DemoDialogListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivityNotRequireViewModel<ActivityMainBinding>(), DemoDialogListener {
    @Inject
    lateinit var appNavigation: AppNavigation
    @Inject
    lateinit var networkConnectionManager: NetworkConnectionManager
    @Inject
    lateinit var rxPreferences: RxPreferences
    var sharedPreferences: SharedPrefs? = null

    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        appNavigation.bind(navHostFragment.navController)

        networkConnectionManager.isNetworkConnectedFlow
            .onEach {
                if (it) {
                    Log.d("ahihi", "onCreate: Network connected")
                } else {
                    Log.d("ahihi", "onCreate: Network disconnected")
                }
            }
            .launchIn(lifecycleScope)
    }

    override fun attachBaseContext(newBase: Context?) {
        sharedPreferences = SharedPrefs(newBase!!)
        val languageCode: String = sharedPreferences!!.locale
        val context: Context = LanguageConfig.changeLanguage(newBase, languageCode)
        super.attachBaseContext(context)
    }

    override fun onStart() {
        super.onStart()
        networkConnectionManager.startListenNetworkState()
    }

    override fun onStop() {
        super.onStop()
        networkConnectionManager.stopListenNetworkState()
    }

    override fun onClickOk() {
        "Ok Activity".toast(this)
    }

    override fun onClickCancel() {
        "Cancel Activity".toast(this)
    }

}