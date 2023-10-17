package com.example.baseproject.ui.authentication

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.toColorInt
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentSignupBinding
import com.example.baseproject.extension.validate
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.utils.Response
import com.example.core.base.BaseFragment
import com.example.core.pref.RxPreferences
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignupFragment :
    BaseFragment<FragmentSignupBinding, SignupViewModel>(R.layout.fragment_signup) {
    private val viewModel: SignupViewModel by viewModels()
    override fun getVM() = viewModel

    @Inject
    lateinit var appNavigation: AppNavigation

    @Inject
    lateinit var rxPreferences: RxPreferences

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        val ss = SpannableString(binding.tvHaveAnAccountLogIn.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = "#4356B4".toColorInt()
                ds.isUnderlineText = false
            }
        }
        var text = getString(R.string.log_in)
        val start = binding.tvHaveAnAccountLogIn.text.indexOf(text)
        val end = start + text.length
        ss.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvHaveAnAccountLogIn.text = ss
        binding.tvHaveAnAccountLogIn.movementMethod = LinkMovementMethod.getInstance()

        text = getString(R.string.terms)
        val ss1 = SpannableString(binding.tvTermsOfService.text)
        val clickableTerm =
            getClickAbleSpan(getString(R.string.terms_of_service), getString(R.string.terms_and_conditions))
        val startT = binding.tvTermsOfService.text.indexOf(getString(R.string.terms))
        val endT = startT + text.length

        val clickablePrivacy = getClickAbleSpan(
            "Privacy Policy",
            getString(R.string.privacy_policy)
        )
        text = getString(R.string.privacy_policy)
        val startP = binding.tvTermsOfService.text.indexOf(text)
        val endP = startP + text.length

        ss1.setSpan(clickableTerm, startT, endT, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ss1.setSpan(clickablePrivacy, startP, endP, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTermsOfService.text = ss1
        binding.tvTermsOfService.movementMethod = LinkMovementMethod.getInstance()

        binding.btnAgreeTermsOfService.isSelected = false
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.apply {
            btnSignUp.setOnClickListener {
                if (!btnAgreeTermsOfService.isSelected) {
                    "Please agree to the terms of service".toast(requireContext())
                    return@setOnClickListener
                }
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val userName = etName.text.toString().trim()
                viewModel.signUp(email, password, userName)
            }
            lifecycleScope.launch {
                rxPreferences.setEmail(etEmail.text.toString().trim())
            }
            btnBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            btnAgreeTermsOfService.setOnClickListener {
                if (btnAgreeTermsOfService.isSelected) {
                    binding.btnSignUp.isEnabled = false
                    binding.btnAgreeTermsOfService.isSelected = false
                    binding.btnAgreeTermsOfService.setBackgroundResource(R.drawable.ic_uncheck)
                } else {
                    binding.btnSignUp.isEnabled = true
                    binding.btnAgreeTermsOfService.isSelected = true
                    binding.btnAgreeTermsOfService.setBackgroundResource(R.drawable.ic_check)
                }
            }
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.apply {
            signUpResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Response.Loading -> {
                        binding.etEmail.isEnabled = false
                        binding.etPassword.isEnabled = false
                        binding.etName.isEnabled = false
                        binding.ProgressBar.visibility = View.VISIBLE
                    }

                    is Response.Failure -> {
                        binding.etEmail.isEnabled = true
                        binding.etPassword.isEnabled = true
                        binding.etName.isEnabled = true
                        binding.ProgressBar.visibility = View.GONE
                        "Sign up failed".toast(requireContext())
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }

                    is Response.Success -> {
                        binding.etEmail.isEnabled = true
                        binding.etPassword.isEnabled = true
                        binding.etName.isEnabled = true
                        binding.ProgressBar.visibility = View.GONE
                        "Sign up successfully".toast(requireContext())
                        appNavigation.openSignupToHomeScreen()
                    }
                }
                validator.observe(viewLifecycleOwner) { validator ->
                    binding.btnSignUp.isEnabled = validator
                }
            }
        }

        binding.apply {
            etEmail.validate { email ->
                if (email.isEmpty()) {
                    etEmail.error =
                        getString(R.string.do_not_leave_this_field_blank)
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.error = getString(R.string.invalid_email_address)
                } else {
                    etEmail.error = null
                    viewModel.setValidState(isValidEmail = true)
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
                    viewModel.setValidState(isValidPassword = true)
                }
            }
            etName.validate { username ->
                if (username.isEmpty()) {
                    etName.error = getString(R.string.do_not_leave_this_field_blank)
                } else {
                    etName.error = null
                    viewModel.setValidState(isValidUserName = true)
                }
            }
        }

    }

    fun getClickAbleSpan(title: String, des: String): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                // show alert dialog to display term
                val dialog = AlertDialog.Builder(requireContext())
                val textView = TextView(requireContext())

                textView.text = title
                textView.textSize = 20.0F
                textView.setTypeface(null, Typeface.BOLD)
                textView.gravity = Gravity.CENTER

                dialog.setCustomTitle(textView)
                dialog.setMessage(des)
                dialog.setNeutralButton("Close") { dialog, which ->
                    dialog.dismiss()
                }
                dialog.show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = "#4356B4".toColorInt()
                ds.isUnderlineText = false
            }
        }
    }

}