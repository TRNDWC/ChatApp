package com.example.baseproject.ui.friend

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentFriendBinding
import com.example.baseproject.ui.friend.tabs.AllFragment
import com.example.baseproject.ui.friend.tabs.FriendsFragment
import com.example.baseproject.ui.friend.tabs.RequestFragment
import com.example.baseproject.ui.friend.tabs.SearchFriendFragment
import com.example.baseproject.utils.Response
import com.example.core.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        binding.tvCancel.visibility = View.GONE
        if (childFragmentManager.backStackEntryCount > 0) {
            childFragmentManager.popBackStack()
        }

        for (i in 0..2) {
            setUpTabLayout(i)
        }

    }

    override fun bindingStateView() {
        super.bindingStateView()

        viewModel.numberOfResquest.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {
                    binding.tabs.getTabAt(2)?.text = getString(R.string.requests)
                }

                is Response.Success -> {
                    setUpTabLayout(2, response.data)
                }

                is Response.Failure -> {
                    binding.tabs.getTabAt(2)?.text = getString(R.string.requests)
                }
            }
        }

        searchAction()

    }

    override fun setOnClick() {
        super.setOnClick()
        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.fragmentContainerView.visibility = View.VISIBLE
                binding.viewPager.visibility = View.GONE
                binding.tabs.visibility = View.GONE
                binding.tvCancel.visibility = View.VISIBLE
                if (childFragmentManager.findFragmentByTag("search") == null) {
                    childFragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.slide_in_up,
                        R.anim.slide_out_down,
                        R.anim.slide_in_up,
                        R.anim.slide_out_down
                    ).add(
                        R.id.fragmentContainerView, SearchFriendFragment.newInstance(), "search"
                    ).addToBackStack(null).commit()
                } else {
                    childFragmentManager.popBackStack()
                    childFragmentManager.beginTransaction().add(
                        R.id.fragmentContainerView, SearchFriendFragment.newInstance(), "search"
                    ).addToBackStack(null).commit()
                }
            }
        }

        binding.tvCancel.setOnClickListener {
            back()
            binding.tvCancel.visibility = View.GONE
        }

        viewModel.isBackFromSearch.observe(viewLifecycleOwner) {
            if (it) {
                back()
                viewModel.isBackFromSearch.postValue(false)
            }
        }

        binding.searchView.findViewById<View>(R.id.search_close_btn).setOnClickListener {
            binding.searchView.setQuery("", false)
            binding.searchView.clearFocus()
        }
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

    private fun back() {
        binding.searchView.clearFocus()
        childFragmentManager.popBackStack()
        binding.fragmentContainerView.postDelayed({
            binding.fragmentContainerView.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
            binding.tabs.visibility = View.VISIBLE
        }, 300)
    }

    private fun searchAction() {

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.query.postValue(newText)
                return true
            }
        })
    }

    private fun setUpTabLayout(index: Int, number: Int = 0) {

        val list = listOf(
            getString(R.string.friends),
            getString(R.string.all),
            getString(R.string.requests)
        )

        if (number > 0) {
            val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
            val view =
                LayoutInflater.from(requireContext()).inflate(R.layout.tab_layout, null)
            val textView = view.findViewById<TextView>(R.id.tv_tab_title)
            textView.text = list[index].uppercase()
            textView.typeface = boldTypeface
            val dot = view.findViewById<TextView>(R.id.tv_tab_notice)
            dot.visibility = View.VISIBLE
            dot.text = number.toString()
            dot.typeface = boldTypeface
            binding.tabs.getTabAt(index)?.customView = view
        } else {
            val view =
                LayoutInflater.from(requireContext()).inflate(R.layout.tab_layout, null)
            view.findViewById<TextView>(R.id.tv_tab_title).text =
                list[index].uppercase()

            view.findViewById<TextView>(R.id.tv_tab_notice).visibility = View.GONE
            binding.tabs.getTabAt(index)?.customView = view
        }
    }

}