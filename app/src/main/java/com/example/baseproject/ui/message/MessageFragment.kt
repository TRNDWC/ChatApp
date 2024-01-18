package com.example.baseproject.ui.message

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentMessageBinding
import com.example.baseproject.model.Chat
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.FriendState
import com.example.baseproject.model.Profile
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.home.HomeViewModel
import com.example.baseproject.ui.message.adapter.MessageAdapter
import com.example.baseproject.ui.message.adapter.OnMessageItemClicked
import com.example.baseproject.utils.Response
import com.example.core.base.BaseFragment
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessageFragment :
    BaseFragment<FragmentMessageBinding, MessageViewModel>(R.layout.fragment_message),
    OnMessageItemClicked {

    private val viewModel: MessageViewModel by viewModels()
    override fun getVM() = viewModel
    private lateinit var messageAdapter: MessageAdapter

    @Inject
    lateinit var appNavigation: AppNavigation


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.searchView.queryHint = getString(R.string.search_messages_here)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        viewModel.message.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    response.data.let {
                        messageAdapter = MessageAdapter(it, this)
                        binding.rcvMessage.adapter = messageAdapter
                    }
                }

                is Response.Failure -> {
                }

                is Response.Loading -> {
                }
            }
        }
        binding.rcvMessage.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    override fun onMessageItemClicked(chat: Chat) {
        val bundle = Bundle()
        val friend = FriendModel(
            id = chat.friendId,
            name = chat.name,
            profileImage = chat.image,
            state = FriendState.FRIEND
        )
        bundle.putParcelable("friend", friend)
        appNavigation.openChatScreen(bundle)
    }

}