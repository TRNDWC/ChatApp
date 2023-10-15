package com.example.baseproject.ui.chat

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentChatBinding
import com.example.baseproject.model.Message
import com.example.baseproject.model.MessageStatus
import com.example.baseproject.model.MessageType
import com.example.baseproject.ui.message.MessageViewModel
import com.example.baseproject.ui.message.adapter.MessageAdapter
import com.example.core.base.BaseFragment
import com.example.core.utils.toast

class ChatFragment() : BaseFragment<FragmentChatBinding, ChatViewModel>(R.layout.fragment_chat) {
    private val viewModel: ChatViewModel by viewModels()
    override fun getVM() = viewModel
    private lateinit var chatAdapter: ChatAdapter

    companion object {
        fun newInstance() = ChatFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

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

        setupRecyclerView()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChat.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                binding.rvChat.postDelayed(Runnable {
                    binding.rvChat.smoothScrollToPosition(
                        binding.rvChat.adapter!!.itemCount - 1
                    )
                }, 100)
            }
        }



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
                binding.edtMessageInput.text.toString().toast(requireContext())
            }
            binding.rvChat.scrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(DummyData())
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.rvChat.scrollToPosition(chatAdapter.itemCount - 1)
    }

    private fun DummyData(): List<Message> {
        val list = mutableListOf<Message>()
        for (i in 0..10) {
            list.add(
                Message(
                    "Hi $i",
                    "1",
                    "1",
                    "Hi $i",
                    1,
                    MessageStatus.RECEIVED,
                    MessageType.TEXT
                )
            )
            list.add(
                Message(
                    "Hello $i",
                    "1",
                    "1",
                    "Hello $i",
                    1,
                    MessageStatus.SENT,
                    MessageType.TEXT
                )
            )
            list.add(
                Message(
                    "Nice to meet you $i",
                    "1",
                    "1",
                    "Nice to meet you $i",
                    1,
                    MessageStatus.RECEIVED,
                    MessageType.TEXT
                )
            )
            list.add(
                Message(
                    "Nice to meet you too $i",
                    "1",
                    "1",
                    "Nice to meet you too $i",
                    1,
                    MessageStatus.SENT,
                    MessageType.TEXT
                )
            )
            list.add(
                Message(
                    "This is a long text to test my UI $i",
                    "1",
                    "1",
                    "This is a long text to test my UI $i",
                    1,
                    MessageStatus.RECEIVED,
                    MessageType.TEXT
                )
            )
            list.add(
                Message(
                    "This is another long text to test my UI too and the result is quite oke$i",
                    "1",
                    "1",
                    "This is another long text to test my UI too and the result is quite oke$i",
                    1,
                    MessageStatus.RECEIVED,
                    MessageType.TEXT
                )
            )
        }
        return list
    }

}