package com.example.baseproject.ui.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentEditProfileBinding
import com.example.baseproject.model.Profile
import com.example.core.base.BaseFragment
import com.example.core.utils.toast
import com.github.dhaval2404.imagepicker.ImagePicker

class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding, ProfileViewModel>(R.layout.fragment_edit_profile) {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private val viewModel: ProfileViewModel by activityViewModels()
    override fun getVM(): ProfileViewModel = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        // load anh profile tu firebase
        viewModel.mProfile.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.example.baseproject.utils.Response.Failure -> {
                    "Error".toast(requireContext())
                }

                is com.example.baseproject.utils.Response.Loading -> {
                }

                is com.example.baseproject.utils.Response.Success -> {
                    if (response.data.profilePictureUri != null)
                        Glide.with(requireContext())
                            .load(response.data.profilePictureUri?.toUri())
                            .into(binding.imgAvatar)
                    else
                        binding.imgAvatar.setImageResource(R.drawable.ic_profile)
                }
            }
        }
        binding.edtBirthDay.isFocusable = false
        binding.edtName.isSingleLine = true
        binding.edtPhoneNumber.isSingleLine = true
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.editProfilePictureButton.setOnClickListener {
            // chon anh tu thu vien
            ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .crop(12f, 16f)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        binding.backArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.edtBirthDay.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    binding.edtBirthDay.setText("$dayOfMonth/${month + 1}/$year")
                },
                2000,
                1,
                1
            ).show()
        }

        binding.txSave.setOnClickListener {
            viewModel.updateProfile(
                Profile(
                    "",
                    name = binding.edtName.text.toString(),
                    profilePictureUri = viewModel.nAvatar.value,
                    email = "",
                    phoneNumber = binding.edtPhoneNumber.text.toString(),
                    DOB = binding.edtBirthDay.text.toString()
                )
            )
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                binding.imgAvatar.setImageURI(fileUri)
                viewModel.nAvatar.value = fileUri.toString()
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                ImagePicker.getError(data).toast(requireContext())
            } else {
                "Task Cancelled".toast(requireContext())
            }
        }
}