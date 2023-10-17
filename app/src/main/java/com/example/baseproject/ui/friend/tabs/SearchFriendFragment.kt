package com.example.baseproject.ui.friend.tabs

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentSearchFriendBinding
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.FriendState
import com.example.baseproject.navigation.AppNavigation
import com.example.baseproject.ui.chat.ChatFragment
import com.example.baseproject.ui.friend.FriendViewModel
import com.example.baseproject.ui.friend.adpater.FriendTabAdapter
import com.example.baseproject.ui.friend.adpater.OnFriendItemClicked
import com.example.baseproject.ui.friend.adpater.SearchFriendAdapter
import com.example.core.base.BaseFragment
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFriendFragment :
    BaseFragment<FragmentSearchFriendBinding, FriendViewModel>(R.layout.fragment_search_friend),
    OnFriendItemClicked {

    private val viewModel: FriendViewModel by viewModels({ requireParentFragment() })
    override fun getVM() = viewModel

    private lateinit var searchFriendAdapter: SearchFriendAdapter
    private lateinit var friendList: List<FriendModel>

    @Inject
    lateinit var appNavigation: AppNavigation

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

    override fun setOnClick() {
        super.setOnClick()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.isBackFromSearch.postValue(true)
        }
    }

    override fun bindingStateView() {
        super.bindingStateView()
        viewModel.mFriendModelList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is com.example.baseproject.utils.Response.Loading -> {
                }

                is com.example.baseproject.utils.Response.Success -> {
                    friendList = response.data
                }

                is com.example.baseproject.utils.Response.Failure -> {
                }
            }
        }
        viewModel.query.observe(viewLifecycleOwner) { query ->
            if (query.isEmpty()) {
                binding.rcvResult.visibility = View.GONE
                binding.txtNoResult.visibility = View.GONE
                binding.imgNoResult.visibility = View.GONE
            } else {
                val filteredList = getFilteredList(friendList, query)
                if (filteredList.isEmpty()) {
                    binding.rcvResult.visibility = View.GONE
                    binding.txtNoResult.visibility = View.VISIBLE
                    binding.imgNoResult.visibility = View.VISIBLE
                } else {
                    binding.rcvResult.visibility = View.VISIBLE
                    binding.txtNoResult.visibility = View.GONE
                    binding.imgNoResult.visibility = View.GONE
                    searchFriendAdapter = SearchFriendAdapter(filteredList, this)
                    binding.rcvResult.apply {
                        adapter = searchFriendAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFriendFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onFriendItemClicked(friend: FriendModel) {
        val bundle = Bundle()
        bundle.putParcelable("friend", friend)
        appNavigation.openChatScreen(
            bundle
        )
    }

    private fun getFilteredList(list: List<FriendModel>, query: String): List<FriendModel> {
        val filteredList = ArrayList<FriendModel>()
        for (item in list) {
            if (item.name!!.lowercase().contains(query.lowercase())) {
                filteredList.add(item)
            }
        }
        return filteredList
    }
}