package com.example.baseproject.ui.friend.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentFriendsBinding
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.FriendState
import com.example.baseproject.model.Profile
import com.example.baseproject.ui.friend.FriendViewModel
import com.example.baseproject.ui.friend.adpater.FriendTabAdapter
import com.example.baseproject.ui.friend.adpater.OnFriendItemClicked
import com.example.baseproject.utils.Response
import com.example.core.base.BaseFragment
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment :
    BaseFragment<FragmentFriendsBinding, FriendViewModel>(R.layout.fragment_friends),
    OnFriendItemClicked {

    private val viewModel: FriendViewModel by viewModels({ requireParentFragment() })
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
        viewModel.mFriendModelList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
//                    lọc ra những bạn bè đã chấp nhận
                    val listFriend = response.data.filter {
                        it.state == FriendState.FRIEND
                    }
                    friendTabAdapter = FriendTabAdapter(listFriend, this)
                    binding.recyclerViewFriends.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = friendTabAdapter
                    }
                }

                is Response.Failure -> {
                }

                else -> {}
            }
        }
    }

    override fun onFriendItemClicked(friend: FriendModel) {
    }

}