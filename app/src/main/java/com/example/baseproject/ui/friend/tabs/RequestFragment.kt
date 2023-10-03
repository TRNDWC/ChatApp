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
import com.example.baseproject.databinding.FragmentRequestBinding
import com.example.baseproject.databinding.LayoutRequestItemBinding
import com.example.baseproject.model.Profile
import com.example.baseproject.ui.friend.FriendViewModel
import com.example.baseproject.ui.friend.adpater.OnRequestItemClicked
import com.example.baseproject.ui.friend.adpater.RequestTabAdapter
import com.example.core.base.BaseFragment
import com.example.core.utils.toast

class RequestFragment :
    BaseFragment<FragmentRequestBinding, FriendViewModel>(R.layout.fragment_request),
    OnRequestItemClicked {

    private val viewModel: FriendViewModel by viewModels()
    override fun getVM() = viewModel
    private lateinit var requestTabAdapter: RequestTabAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_request, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RequestFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        requestTabAdapter = RequestTabAdapter(
            dummyData().toMutableList(), this, 66
        )

        binding.recyclerViewReceivedRequest.adapter = requestTabAdapter

        binding.recyclerViewReceivedRequest.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    private fun dummyData(): MutableList<Profile> {
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

    override fun onItemClicked(view: LayoutRequestItemBinding, profile: Profile) {
        when (view.txtProgress.text) {
            getString(R.string.accept) -> {
                requireContext().toast("Now you are friend with ${profile.name}")
            }
            getString(R.string.cancel_request) -> {
                requireContext().toast("Request canceled")
            }
            else -> {
                requireContext().toast("Request refused")
            }
        }
        view.crdProgress.isClickable = false
    }

    override fun onLongItemClicked(view: LayoutRequestItemBinding, profile: Profile) {
        val btn = view.crdProgress
        val bar = view.progressBar

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
                view.txtProgress.text = getString(R.string.refuse)
                view.txtProgress.setTextColor(Color.parseColor("#4356B4"))
                bar.progressDrawable = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.rounded_button_unselected
                )
            }
        }.start()
    }
}