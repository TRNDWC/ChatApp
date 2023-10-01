package com.example.baseproject.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.container.MainActivity
import com.example.baseproject.databinding.FragmentProfileBinding
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.utils.LanguageConfig
import com.example.baseproject.utils.LanguageConfig.changeLanguage
import com.example.baseproject.utils.SharedPrefs
import com.example.core.base.BaseFragment
import com.example.core.utils.setDrawableCompat
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    @Inject
    lateinit var appNavigation: AppNavigation

    private val viewModel: ProfileViewModel by viewModels()
    override fun getVM(): ProfileViewModel = viewModel

    lateinit var sharedPreferences: SharedPrefs

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE
        binding.imgBackground.visibility = View.GONE
        binding.layoutProfileMenu.visibility = View.GONE

        viewModel.mProfile.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.example.baseproject.utils.Response.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.imgBackground.visibility = View.GONE
                    binding.layoutProfileMenu.visibility = View.GONE
                    "Error".toast(requireContext())
                }

                is com.example.baseproject.utils.Response.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.imgBackground.visibility = View.GONE
                    binding.layoutProfileMenu.visibility = View.GONE
                }

                is com.example.baseproject.utils.Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.imgBackground.visibility = View.VISIBLE
                    binding.layoutProfileMenu.visibility = View.VISIBLE
                    binding.tvName.text = response.data.name
                    binding.tvEmail.text = response.data.email


                    Glide.with(requireContext())
                        .load(response.data.profilePictureUri?.toUri())
                        .into(binding.imgBackground)

                    // tai anh vao layer trong imageview
                    Glide.with(requireContext())
                        .load(response.data.profilePictureUri?.toUri())
                        .circleCrop()
                        .into(binding.imageView)


                }
            }
        }
        viewModel.logOutResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.example.baseproject.utils.Response.Failure -> {
                    "Error".toast(requireContext())
                }

                is com.example.baseproject.utils.Response.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is com.example.baseproject.utils.Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    appNavigation.openProfileToLoginScreen()
                }
            }
        }

        if (sharedPreferences.locale == "en") {
            binding.tvEnglish.text = resources.getString(R.string.english)
        } else {
            binding.tvEnglish.text = resources.getString(R.string.vietnamese)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = SharedPrefs(context)
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.layoutLogout.setOnClickListener {
            viewModel.logOut()
        }

        binding.btnEditProfile.setOnClickListener {
            appNavigation.openEditProfileScreen()
        }

        binding.btnArrowRightLanguage.setOnClickListener {
            if (sharedPreferences.locale == "en") {
                sharedPreferences.locale = "vi"
                binding.tvEnglish.text = resources.getString(R.string.vietnamese)
                changeLanguage(requireContext(), "vi")
            } else {
                sharedPreferences.locale = "en"
                binding.tvEnglish.text = resources.getString(R.string.english)
                changeLanguage(requireContext(), "en")
            }
            (activity as MainActivity).recreate()
        }

    }
}