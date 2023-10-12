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
import com.example.baseproject.model.Profile
import com.example.baseproject.ui.home.HomeViewModel
import com.example.baseproject.ui.message.adapter.MessageAdapter
import com.example.baseproject.ui.message.adapter.OnMessageItemClicked
import com.example.core.base.BaseFragment
import com.example.core.utils.toast

class MessageFragment :
    BaseFragment<FragmentMessageBinding, MessageViewModel>(R.layout.fragment_message),
    OnMessageItemClicked {

    private val viewModel: MessageViewModel by viewModels()
    override fun getVM() = viewModel
    private lateinit var messageAdapter: MessageAdapter

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.searchView.queryHint = getString(R.string.search_messages_here)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(
            dummyData(), this
        )
        binding.rcvMessage.adapter = messageAdapter
        binding.rcvMessage.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun dummyData(): List<Profile> {
        val list = mutableListOf<Profile>()
        for (i in 'a'..'z') {
            list.add(Profile("",i.toString(), null, i.toString(), i.toString(), i.toString()))
            list.add(Profile("",i.toString(), null, i.toString(), i.toString(), i.toString()))
            list.add(Profile("",i.toString(), null, i.toString(), i.toString(), i.toString()))
            list.add(Profile("",i.toString(), null, i.toString(), i.toString(), i.toString()))
            list.add(Profile("",i.toString(), null, i.toString(), i.toString(), i.toString()))
        }
        return list
    }

    override fun onMessageItemClicked(profile: Profile) {
        "${profile.name} clicked".toast(requireContext())
    }

}