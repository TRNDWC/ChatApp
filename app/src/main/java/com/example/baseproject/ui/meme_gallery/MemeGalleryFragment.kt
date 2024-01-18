package com.example.baseproject.ui.meme_gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentMemeGalleryBinding
import com.example.baseproject.databinding.LayoutImageGalleryItemBinding
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.Meme
import com.example.baseproject.ui.chat.ChatViewModel
import com.example.core.base.BaseFragment
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemeGalleryFragment(
    val friend: FriendModel
) : BaseFragment<FragmentMemeGalleryBinding, ChatViewModel>(R.layout.fragment_meme_gallery),
    OnMemeClickListener {

    companion object {
        fun newInstance(friend: FriendModel) = MemeGalleryFragment(friend)
    }

    lateinit var memeAdapter: MemeAdapter
    private val viewModel: ChatViewModel by viewModels()
    override fun getVM() = viewModel


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        viewModel.getMemes()
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.listMemes.observe(viewLifecycleOwner) {
            if (it != null) {
                memeAdapter = MemeAdapter(it, this)
                binding.include.recyclerView.adapter = memeAdapter
                val gridLayoutManager = GridLayoutManager(requireContext(), 3)
                binding.include.recyclerView.layoutManager = gridLayoutManager
            }
        }
        binding.include.btnSend.visibility = View.GONE
    }

    override fun onMemeClick(binding: LayoutImageGalleryItemBinding, meme: Meme) {
        viewModel.sendMemeTo(meme.url, friend)
        "Đã gửi".toast(requireContext())
    }

}