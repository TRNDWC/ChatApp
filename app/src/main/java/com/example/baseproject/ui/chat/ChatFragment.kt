package com.example.baseproject.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentChatBinding
import com.example.baseproject.databinding.LayoutRoomItemBinding
import com.example.baseproject.extension.scrollToBottom
import com.example.baseproject.extension.validate
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.Message
import com.example.baseproject.model.MessageStatus
import com.example.baseproject.model.MessageType
import com.example.baseproject.ui.image_gallery.ImageGalleryFragment
import com.example.baseproject.ui.meme_gallery.MemeGalleryFragment
import com.example.core.base.BaseFragment
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatFragment : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat),
    OnClickItemListener {
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
        binding.rvChat.scrollToBottom()

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

        binding.guideline.setGuidelinePercent(1f)
        binding.imgOpenGallery.visibility = View.VISIBLE
        binding.edtMessageInput.clearFocus()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChat.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom && chatAdapter.itemCount > 0) {
                binding.rvChat.scrollToBottom()
            }
        }

        viewModel.mMessages.observe(viewLifecycleOwner) {
            var lastAdapterMessage: Message? = null
            if (chatAdapter.itemCount > 0)
                lastAdapterMessage = chatAdapter.mMessage.last()
            if (it is com.example.baseproject.utils.Response.Success) {
                if (lastAdapterMessage == null) {
                    chatAdapter.setMessages(it.data)
                    binding.rvChat.scrollToBottom()
                } else {
                    val lastMessage = it.data.last()
                    if (lastMessage.messageTime > lastAdapterMessage.messageTime && lastMessage.messageStatus == MessageStatus.SENT) {
                        chatAdapter.setMessages(it.data)
                        binding.rvChat.scrollToBottom()
                    } else {
                        chatAdapter.setMessages(it.data)
                    }
                }
            }
        }

        binding.rvChat.addOnScrollListener(object :
            androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItemPosition =
                    (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstVisibleItemPosition()
                if (firstVisibleItemPosition <= 5) {
                    viewModel.loadMoreMessages(friend, lastMessage)
                }
            }
        })

        viewModel.readMessage(friend)
        listenEdittextChat()
        touchListener(view)
    }

    override fun setOnClick() {
        super.setOnClick()
        binding.btnBack.setOnClickListener(this::clickHandler)
        binding.imgOpenGallery.setOnClickListener(this::clickHandler)
        binding.imgSend.setOnClickListener(this::clickHandler)
        binding.imgEmoji.setOnClickListener(this::clickHandler)
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(mutableListOf(), this)
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun listenEdittextChat() {
        binding.apply {
            edtMessageInput.validate { textInputUser ->
                if (textInputUser.isBlank()) imgSend.setImageResource(R.drawable.ic_like)
                else imgSend.setImageResource(R.drawable.ic_sent)
            }

            edtMessageInput.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    binding.guideline.setGuidelinePercent(1f)
                    binding.fragmentContainerView.visibility = View.GONE
                }
            }
        }
    }

    private fun clickHandler(view: View) {
        when (view.id) {

            R.id.btn_back -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            R.id.img_open_gallery -> {
                binding.edtMessageInput.clearFocus()
                binding.guideline.setGuidelinePercent(0.6f)
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, ImageGalleryFragment.newInstance(friend))
                    .commit()
                binding.fragmentContainerView.visibility = View.VISIBLE
            }

            R.id.img_send -> {
                if (binding.edtMessageInput.text.toString().isNotBlank()) {
                    viewModel.sendMessageTo(
                        Message(
                            content = binding.edtMessageInput.text.toString(),
                            chatId = "",
                            messageTime = System.currentTimeMillis(),
                            receivedId = "",
                            id = "",
                            messageStatus = MessageStatus.SENT,
                            messageType = MessageType.TEXT,
                        ), friend
                    )
                    binding.edtMessageInput.setText("")
                }
            }

            R.id.img_emoji -> {
                binding.guideline.setGuidelinePercent(0.6f)
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, MemeGalleryFragment.newInstance(friend))
                    .commit()
                binding.fragmentContainerView.visibility = View.VISIBLE
                binding.edtMessageInput.clearFocus()
                hideKeyboard()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun touchListener(view: View) {
        view.setOnTouchListener { v, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                binding.guideline.setGuidelinePercent(1f)
                binding.edtMessageInput.clearFocus()
                binding.fragmentContainerView.visibility = View.GONE
            }
            true
        }
    }

    override fun onClickItem(view: LayoutRoomItemBinding, position: Int) {
        touchListener(view.root)
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}