package com.example.baseproject.ui.friend.tabs

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baseproject.R
import com.example.baseproject.databinding.FragmentAllBinding
import com.example.baseproject.databinding.LayoutAllItemBinding
import com.example.baseproject.model.Profile
import com.example.baseproject.ui.friend.FriendViewModel
import com.example.baseproject.ui.friend.adpater.AllTabAdapter
import com.example.baseproject.ui.friend.adpater.OnAllItemClickListener
import com.example.core.base.BaseFragment
import com.example.core.utils.toast

class AllFragment :
    BaseFragment<FragmentAllBinding, FriendViewModel>(R.layout.fragment_all),
    OnAllItemClickListener {

    private val viewModel: FriendViewModel by viewModels()
    override fun getVM() = viewModel

    private lateinit var allTabAdapter: AllTabAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AllFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        allTabAdapter = AllTabAdapter(
            dummyData(), this
        )
        binding.recyclerViewFriends.adapter = allTabAdapter
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

    override fun onAllItemClicked(binding: LayoutAllItemBinding, profile: Profile) {
        binding.progressBar.progressDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.custom_progressbar
        )
        if (binding.txtProgress.text == getString(R.string.add_friend)) {
            binding.txtProgress.text = getString(R.string.request_sent)
            binding.crdProgress.isLongClickable = true
            binding.txtProgress.setTextColor(Color.parseColor("#FFFFFF"))

        } else if (binding.txtProgress.text == getString(R.string.cancel_request)) {
            binding.txtProgress.text = getString(R.string.add_friend)
            binding.crdProgress.isLongClickable = false
            binding.txtProgress.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun onAllItemLongClicked(binding: LayoutAllItemBinding, profile: Profile) {
        val btn = binding.crdProgress
        val bar = binding.progressBar

        object : CountDownTimer(500, 1) {
            override fun onTick(millisUntilFinished: Long) {
                if (!btn.isPressed) {
                    bar.progress = 0
                    cancel()
                } else {
                    bar.progress = 4 + bar.progress
                }
            }

            override fun onFinish() {
                bar.progress = 0
                binding.txtProgress.text = getString(R.string.cancel_request)
                binding.txtProgress.setTextColor(Color.parseColor("#4356B4"))
                bar.progressDrawable = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rounded_button_unselected
                )
            }
        }.start()
    }


}