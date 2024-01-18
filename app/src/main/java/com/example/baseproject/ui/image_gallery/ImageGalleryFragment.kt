package com.example.baseproject.ui.image_gallery

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentImageGalleryBinding
import com.example.baseproject.databinding.LayoutImageGalleryItemBinding
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.Image
import com.example.baseproject.ui.chat.ChatViewModel
import com.example.baseproject.utils.PermissionsUtil.checkPermissions
import com.example.baseproject.utils.PermissionsUtil.requestPermissions
import com.example.core.base.BaseFragment
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageGalleryFragment(
    private val friend: FriendModel
) :
    BaseFragment<FragmentImageGalleryBinding, ChatViewModel>(R.layout.fragment_image_gallery),
    OnImageClickListener {

    companion object {
        fun newInstance(friend: FriendModel) = ImageGalleryFragment(friend)
    }

    private lateinit var galleryAdapter: GalleryAdapter
    private val viewModel: ChatViewModel by viewModels()

    override fun getVM() = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        if (checkPermissions(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            viewModel.getAllImageFromExternalStorage()
            binding.constraintLayout.background = null
            binding.btnRequestPermission.visibility = ViewGroup.GONE
        } else {
            binding.btnRequestPermission.setOnClickListener {
                requestPermissions(
                    requireActivity(),
                    1,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )

                binding.btnRequestPermission.visibility = ViewGroup.GONE
                binding.include.btnSend.visibility = ViewGroup.VISIBLE
            }
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.listImage.observe(viewLifecycleOwner) { list ->
            galleryAdapter = GalleryAdapter(list, this)
            binding.include.recyclerView.apply {
                adapter = galleryAdapter
                layoutManager = androidx.recyclerview.widget.GridLayoutManager(
                    requireContext(),
                    3,
                    androidx.recyclerview.widget.GridLayoutManager.VERTICAL,
                    false
                )
                setHasFixedSize(true)
            }
        }

        binding.include.btnSend.setOnClickListener {
            for (image in galleryAdapter.getSelectedImage()) {
                image.url?.let { it1 -> viewModel.sendImageMessageTo(it1.toUri(), friend) }
                image.isSelected = false
                "Send image success".toast(requireContext())
            }
        }
    }

    override fun onImageClick(binding: LayoutImageGalleryItemBinding, image: Image) {
        image.isSelected = !image.isSelected
        binding.view.visibility = if (image.isSelected) {
            ViewGroup.VISIBLE
        } else {
            ViewGroup.GONE
        }
    }
}