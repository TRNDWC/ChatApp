package com.example.baseproject.ui.friend.adpater

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.LayoutRequestItemBinding
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.Profile

interface OnRequestItemClicked {
    fun onItemClicked(view: LayoutRequestItemBinding, item: FriendModel)
    fun onLongItemClicked(view: LayoutRequestItemBinding, item: FriendModel)
}

class RequestTabAdapter(
    private var mFriendList: MutableList<FriendModel>,
    private val onRequestClickListener: OnRequestItemClicked,
    private val code: Int
) : RecyclerView.Adapter<RequestTabAdapter.RequestTabViewHolder>() {

    inner class RequestTabViewHolder(
        mFriendTabItem: LayoutRequestItemBinding, onRequestClickListener: OnRequestItemClicked
    ) : RecyclerView.ViewHolder(mFriendTabItem.root) {
        val friendName = mFriendTabItem.txtName
        val friendImage = mFriendTabItem.imgAvatar
        val btnProgress = mFriendTabItem.crdProgress
        val barProgress = mFriendTabItem.progressBar
        val txtProgress = mFriendTabItem.txtProgress
        val txtChar = mFriendTabItem.txtChar
        val view = mFriendTabItem.view

        init {
            mFriendTabItem.apply {
                crdProgress.setOnClickListener {
                    onRequestClickListener.onItemClicked(
                        mFriendTabItem,
                        mFriendList[adapterPosition]
                    )
                    mFriendList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                }
                crdProgress.setOnLongClickListener {
                    onRequestClickListener.onLongItemClicked(
                        mFriendTabItem,
                        mFriendList[adapterPosition]
                    )
                    true
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestTabViewHolder {
        val mRequestTabItem: LayoutRequestItemBinding = LayoutRequestItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RequestTabViewHolder(mRequestTabItem, onRequestClickListener)
    }

    override fun getItemCount(): Int {
        return mFriendList.size
    }

    override fun onBindViewHolder(holder: RequestTabViewHolder, position: Int) {
        val item = mFriendList[position]

        holder.friendName.text = item.name
        holder.friendImage.setImageResource(R.drawable.ic_profile)
        if (item.profileImage != null) {
            Glide.with(holder.itemView.context)
                .load(item.profileImage.toUri())
                .circleCrop()
                .into(holder.friendImage)
        }
        holder.txtChar.visibility = ViewGroup.GONE
        holder.view.visibility = ViewGroup.GONE

        if (position == 0 || position == code) {
            holder.txtChar.visibility = ViewGroup.VISIBLE
            if (position == 0) {
                holder.txtChar.text = holder.itemView.context.getString(R.string.all_requests)
            } else {
                holder.txtChar.text = holder.itemView.context.getString(R.string.sent_requests)
                holder.view.visibility = ViewGroup.VISIBLE
            }
        }

        if (position < code) {
            holder.barProgress.progressDrawable =
                holder.itemView.context.getDrawable(R.drawable.custom_progressbar)
            holder.txtProgress.setTextColor(Color.parseColor("#FFFFFF"))
            holder.txtProgress.text = holder.itemView.context.getString(R.string.accept)
            holder.btnProgress.isClickable = true
            holder.btnProgress.isLongClickable = true
        } else {
            holder.barProgress.progressDrawable =
                holder.itemView.context.getDrawable(R.drawable.rounded_button_unselected)
            holder.txtProgress.setTextColor(Color.parseColor("#4356B4"))
            holder.txtProgress.text = holder.itemView.context.getString(R.string.cancel_request)
            holder.btnProgress.isClickable = true
            holder.btnProgress.isLongClickable = false
        }
    }
}