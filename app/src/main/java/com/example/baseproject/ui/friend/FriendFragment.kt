package com.example.baseproject.ui.friend

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentFriendBinding
import com.example.baseproject.ui.friend.tabs.AllFragment
import com.example.baseproject.ui.friend.tabs.FriendsFragment
import com.example.baseproject.ui.friend.tabs.RequestFragment
import com.example.core.base.BaseFragment


class FriendFragment :
    BaseFragment<FragmentFriendBinding, FriendViewModel>(R.layout.fragment_friend) {

    companion object {
        fun newInstance() = FriendFragment()
    }

    private val viewModel: FriendViewModel by viewModels()
    override fun getVM() = viewModel

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        setupViewPager()
        binding.searchView.queryHint = getString(R.string.search_friends_here)

    }

    private fun setupViewPager() {

        val adapter = PagerAdapter(childFragmentManager)

        val friendsFragment: FriendsFragment = FriendsFragment.newInstance()
        val allFragment: AllFragment = AllFragment.newInstance()
        val requestFragment: RequestFragment = RequestFragment.newInstance()

        adapter.addFragment(friendsFragment, getString(R.string.friends))
        adapter.addFragment(allFragment, getString(R.string.all))
        adapter.addFragment(requestFragment, getString(R.string.requests))

        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }
}