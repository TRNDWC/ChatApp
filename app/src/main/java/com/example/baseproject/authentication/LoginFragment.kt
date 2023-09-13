package com.example.baseproject.authentication

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLoginBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.core.base.BaseFragment
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()
    override fun getVM(): LoginViewModel = viewModel

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        val ss = SpannableString(binding.tvDontHaveAccount.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                appNavigation.openLoginToSignupScreen()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = "#4356B4".toColorInt()
                ds.isUnderlineText = false
            }
        }
        val start = binding.tvDontHaveAccount.text.indexOf("Sign up")
        val end = start + "Sign up".length
        ss.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvDontHaveAccount.text = ss
        binding.tvDontHaveAccount.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun bindingAction() {
        super.bindingAction()
        binding.btnLogin.setOnSafeClickListener {
            appNavigation.openLoginToHomeScreen()
        }
    }

}