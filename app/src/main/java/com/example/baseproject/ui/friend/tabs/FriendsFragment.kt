package com.example.baseproject.ui.friend.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentFriendsBinding
import com.example.baseproject.model.Profile
import com.example.baseproject.ui.friend.FriendViewModel
import com.example.baseproject.ui.friend.adpater.FriendTabAdapter
import com.example.baseproject.ui.friend.adpater.OnFriendItemClicked
import com.example.core.base.BaseFragment
import com.example.core.utils.toast

class FriendsFragment :
    BaseFragment<FragmentFriendsBinding, FriendViewModel>(R.layout.fragment_friends),
    OnFriendItemClicked {

    private val viewModel: FriendViewModel by viewModels()
    override fun getVM() = viewModel
    private lateinit var friendTabAdapter: FriendTabAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FriendsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        friendTabAdapter = FriendTabAdapter(
            dummyData(), this
        )
        binding.recyclerViewFriends.adapter = friendTabAdapter
        binding.recyclerViewFriends.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun dummyData(): List<Profile> {
        val list = mutableListOf<Profile>()
        for (i in 'a'..'z') {
            list.add(Profile(i.toString(), null, i.toString(), i.toString(), i.toString()))
            list.add(Profile(i.toString(), null, i.toString(), i.toString(), i.toString()))
            list.add(Profile(i.toString(), null, i.toString(), i.toString(), i.toString()))
            list.add(Profile(i.toString(), null, i.toString(), i.toString(), i.toString()))
            list.add(Profile(i.toString(), null, i.toString(), i.toString(), i.toString()))
        }
        return list
    }

    override fun onFriendItemClicked(profile: Profile) {
        "Clicked ${profile.name}".toast(requireContext())
    }

}