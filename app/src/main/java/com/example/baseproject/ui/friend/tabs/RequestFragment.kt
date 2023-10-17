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
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.FriendState
import com.example.baseproject.model.Profile
import com.example.baseproject.ui.friend.FriendViewModel
import com.example.baseproject.ui.friend.adpater.OnRequestItemClicked
import com.example.baseproject.ui.friend.adpater.RequestTabAdapter
import com.example.baseproject.utils.Response
import com.example.core.base.BaseFragment
import com.example.core.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestFragment :
    BaseFragment<FragmentRequestBinding, FriendViewModel>(R.layout.fragment_request),
    OnRequestItemClicked {

    private val viewModel: FriendViewModel by viewModels({ requireParentFragment() })
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
        viewModel.mFriendModelList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    val listRequest = response.data.filter {
                        it.state == FriendState.RECEIVED
                    }
                    val listRequestSent = response.data.filter {
                        it.state == FriendState.SENT
                    }
                    val list: MutableList<FriendModel> =
                        (listRequest + listRequestSent) as MutableList<FriendModel>
                    requestTabAdapter = RequestTabAdapter(list, this, listRequest.size)
                    binding.recyclerViewReceivedRequest.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = requestTabAdapter
                    }
                }

                is Response.Failure -> {
                }

                is Response.Loading -> {
                }
            }
        }
    }


    override fun onItemClicked(view: LayoutRequestItemBinding, item: FriendModel) {
        when (view.txtProgress.text) {
            getString(R.string.accept) -> {
                viewModel.acceptFriend(item)
            }

            getString(R.string.cancel_request) -> {
                viewModel.rejectFriend(item.id!!)
            }

            else -> {
                viewModel.rejectFriend(item.id!!)
            }
        }
        view.crdProgress.isClickable = false
    }

    override fun onLongItemClicked(view: LayoutRequestItemBinding, item: FriendModel) {
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