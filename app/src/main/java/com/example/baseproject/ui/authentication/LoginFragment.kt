package com.example.baseproject.ui.authentication

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentLoginBinding
import com.example.baseproject.extension.validate
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.utils.Response
import com.example.core.base.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.setOnSafeClickListener
import com.example.core.utils.toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {
    private val viewModel: LoginViewModel by viewModels()
    override fun getVM(): LoginViewModel = viewModel

    @Inject
    lateinit var appNavigation: AppNavigation

    @Inject
    lateinit var rxPreferences: RxPreferences

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

        var start = binding.tvDontHaveAccount.text.indexOf("Sign up")
        var end = start + "Sign up".length
        if (start == -1) {
            start = binding.tvDontHaveAccount.text.indexOf("Đăng ký")
            end = start + "Đăng ký".length
        }
        ss.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvDontHaveAccount.text = ss
        binding.tvDontHaveAccount.movementMethod = LinkMovementMethod.getInstance()

        binding.etEmail.error = null
        binding.etPassword.error = null
    }

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.apply {
            signInResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                        binding.etEmail.isEnabled = false
                        binding.etPassword.isEnabled = false
                        binding.ProgressBar.visibility = View.VISIBLE
                    }

                    is Response.Failure -> {
                        binding.etEmail.isEnabled = true
                        binding.etPassword.isEnabled = true
                        binding.ProgressBar.visibility = View.GONE
                    }

                    is Response.Success -> {
                        binding.etEmail.isEnabled = true
                        binding.etPassword.isEnabled = true
                        binding.ProgressBar.visibility = View.GONE
                    }
                }
            }
        }

        binding.apply {
            etEmail.validate { email ->
                if (email.isEmpty()) {
                    etEmail.error = getString(R.string.do_not_leave_this_field_blank)
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.error = getString(R.string.invalid_email_address)
                } else {
                    etEmail.error = null
                }
            }
            etPassword.validate { password ->
                if (password.isEmpty()) {
                    etPassword.error = getString(R.string.do_not_leave_this_field_blank)
                } else if (password.length < 8) {
                    etPassword.error =
                        getString(R.string.password_must_be_at_least_8_characters)
                } else {
                    etPassword.error = null
                }
            }
        }

        if (viewModel.isLogin) {
            appNavigation.openLoginToHomeScreen()
        }

    }

    override fun bindingAction() {
        super.bindingAction()
        viewModel.signInResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Response.Loading -> {}
                is Response.Success -> {
                    getString(R.string.login_successfully).toast(requireContext())
                    appNavigation.openLoginToHomeScreen()
                }

                is Response.Failure -> {
                    when (result.e) {
                        is FirebaseAuthInvalidUserException -> {
                            getString(R.string.email_is_not_registered).toast(requireContext())
                        }

                        is IllegalArgumentException -> {
                            getString(R.string.email_or_password_is_empty).toast(requireContext())
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            getString(R.string.password_is_incorrect).toast(requireContext())
                        }

                        is FirebaseNetworkException -> {
                            getString(R.string.no_internet_connection).toast(requireContext())
                        }

                        else -> {
                            getString(R.string.login_failed).toast(requireContext())
                        }
                    }
                }
            }
        }
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.btnLogin.setOnSafeClickListener {
            viewModel.signIn(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
        lifecycleScope.launch {
            rxPreferences.setEmail(binding.etEmail.text.toString())
        }
    }
}