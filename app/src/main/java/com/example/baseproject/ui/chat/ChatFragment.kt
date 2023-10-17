package com.example.baseproject.ui.chat

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentChatBinding
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.Message
import com.example.baseproject.model.MessageStatus
import com.example.baseproject.model.MessageType
import com.example.baseproject.ui.message.MessageViewModel
import com.example.baseproject.ui.message.adapter.MessageAdapter
import com.example.core.base.BaseFragment
import com.example.core.utils.toast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat) {
    private val viewModel: ChatViewModel by viewModels()
    override fun getVM() = viewModel
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var friend: FriendModel
    private var lastMessage: Message? = null


    companion object {
        fun newInstance() = ChatFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        friend = arguments?.getParcelable("friend")!!
        viewModel.getFriend(friend.id!!)
        setupRecyclerView()
        viewModel.mFriend.observe(viewLifecycleOwner) {
            if (it is com.example.baseproject.utils.Response.Success) {
                friend = it.data
                binding.txtName.text = friend.name
                Glide.with(requireContext()).load(friend.profileImage).circleCrop()
                    .into(binding.imgAvatar)
                chatAdapter.setAvatar(friend.profileImage!!)
            }
        }
        viewModel.mChat.observe(viewLifecycleOwner) {
            if (it != null) {
                Glide.with(requireContext()).load(it.image).into(binding.imgAvatar)
                binding.txtName.text = it.name
            }
        }

        binding.imgOpenGallery.visibility = View.VISIBLE
        binding.imgCircle.visibility = View.VISIBLE
        binding.imgSend.setImageResource(R.drawable.ic_like)
        binding.edtMessageInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.imgOpenGallery.visibility = View.GONE
                binding.imgCircle.visibility = View.GONE
                binding.imgSend.setImageResource(R.drawable.ic_sent)

            } else {
                binding.imgOpenGallery.visibility = View.VISIBLE
                binding.imgCircle.visibility = View.VISIBLE
                binding.imgSend.setImageResource(R.drawable.ic_like)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChat.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                binding.rvChat.postDelayed(Runnable {
                    if (chatAdapter.itemCount > 0) {
                        binding.rvChat.smoothScrollToPosition(chatAdapter.itemCount - 1)
                    }
                }, 100)
            }
        }

        viewModel.mMessages.observe(viewLifecycleOwner) {
            if (it is com.example.baseproject.utils.Response.Success) {
                chatAdapter.setMessages(it.data)
                it.data.forEach {
                    Log.d("ChatFragment", it.content)
                }
                binding.rvChat.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadMoreMessages(
                friend, chatAdapter.mMessage.firstOrNull()
            )
            viewModel.loadMessages.observe(viewLifecycleOwner) {
                if (it is com.example.baseproject.utils.Response.Success) {
                    chatAdapter.addMessage(it.data)
                    viewModel.loadMessages.postValue(com.example.baseproject.utils.Response.Loading)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
        viewModel.readMessage(friend)
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.imgSend.setOnClickListener {
            if (binding.edtMessageInput.text.toString().isNotEmpty()) {
                binding.edtMessageInput.text.toString().toast(requireContext())
            }
        }

        binding.imgOpenGallery.setOnClickListener {
            "Open gallery".toast(requireContext())
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.imgSend.setOnClickListener {
            if (binding.edtMessageInput.text.toString().isNotEmpty()) {
                val message = Message(
                    binding.edtMessageInput.text.toString(),
                    friend.id!!,
                    "",
                    binding.edtMessageInput.text.toString(),
                    MessageStatus.SENT,
                    MessageType.TEXT,
                    System.currentTimeMillis()
                )
                viewModel.sendMessageTo(
                    message, friend
                )
                unFocus()
            }
            binding.rvChat.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(mutableListOf())
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun unFocus() {
        binding.edtMessageInput.text.clear()
    }
}