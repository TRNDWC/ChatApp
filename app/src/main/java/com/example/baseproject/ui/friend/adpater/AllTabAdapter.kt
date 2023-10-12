package com.example.baseproject.ui.friend.adpater

import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.LayoutAllItemBinding
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.FriendState
import com.example.baseproject.model.Profile

interface OnAllItemClickListener {
    fun onAllItemClicked(binding: LayoutAllItemBinding, friendModel: FriendModel)
    fun onAllItemLongClicked(binding: LayoutAllItemBinding, friendModel: FriendModel)
}

class AllTabAdapter(
    private var mProfileList: List<FriendModel>,
    private val onAllItemClickListener: OnAllItemClickListener
) : RecyclerView.Adapter<AllTabAdapter.AllTabViewHolder>() {

    inner class AllTabViewHolder(
        mFriendTabItem: LayoutAllItemBinding, onAllItemClickListener: OnAllItemClickListener
    ) : RecyclerView.ViewHolder(mFriendTabItem.root) {
        val friendName = mFriendTabItem.txtName
        val friendImage = mFriendTabItem.imgAvatar
        val friendFirstChar = mFriendTabItem.txtChar
        val btnAddFriend = mFriendTabItem.txtProgress
        val crdProgress = mFriendTabItem.crdProgress
        val barProgress = mFriendTabItem.progressBar


        init {
            mFriendTabItem.apply {
                crdProgress.setOnClickListener {
                    onAllItemClickListener.onAllItemClicked(
                        mFriendTabItem,
                        mProfileList[adapterPosition]
                    )
                }
                crdProgress.setOnLongClickListener {
                    onAllItemClickListener.onAllItemLongClicked(
                        mFriendTabItem,
                        mProfileList[adapterPosition]
                    )
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTabViewHolder {
        val mAllTabItem: LayoutAllItemBinding = LayoutAllItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AllTabViewHolder(mAllTabItem, onAllItemClickListener)
    }

    override fun getItemCount(): Int {
        return mProfileList.size
    }

    override fun onBindViewHolder(holder: AllTabViewHolder, position: Int) {
        val item = mProfileList[position]

        if (position == 0 ||
            getNameFirstChar(mProfileList[position].name) != getNameFirstChar(mProfileList[position - 1].name)
        ) {
            holder.friendFirstChar.visibility = ViewGroup.VISIBLE
            holder.friendFirstChar.text = getNameFirstChar(item.name)
        } else {
            holder.friendFirstChar.visibility = ViewGroup.GONE
        }

        holder.friendName.text = item.name
        holder.friendImage.setImageResource(R.drawable.ic_profile)
        if (item.profileImage?.equals("null") == false) {
            Glide.with(holder.itemView.context)
                .load(item.profileImage.toUri())
                .circleCrop()
                .into(holder.friendImage)
        }

        holder.crdProgress.visibility = ViewGroup.GONE

        Log.d("TAG", "onBindViewHolder: ${mProfileList[position]}")

        when (mProfileList[position].state) {
            FriendState.FRIEND -> {}

            FriendState.RECEIVED -> {
                holder.crdProgress.visibility = ViewGroup.VISIBLE
                holder.btnAddFriend.text = holder.itemView.context.getString(R.string.accept)
                holder.btnAddFriend.setTextColor(Color.parseColor("#FFFFFF"))
                holder.barProgress.progressDrawable =
                    holder.itemView.context.getDrawable(R.drawable.custom_progressbar)
            }

            FriendState.SENT -> {
                holder.crdProgress.visibility = ViewGroup.VISIBLE
                holder.btnAddFriend.text = holder.itemView.context.getString(R.string.request_sent)
                holder.btnAddFriend.setTextColor(Color.parseColor("#FFFFFF"))
                holder.barProgress.progressDrawable =
                    holder.itemView.context.getDrawable(R.drawable.custom_progressbar)
            }

            else -> {
                holder.crdProgress.visibility = ViewGroup.VISIBLE
                holder.btnAddFriend.text = holder.itemView.context.getString(R.string.add_friend)
                holder.btnAddFriend.setTextColor(Color.parseColor("#FFFFFF"))
                holder.barProgress.progressDrawable =
                    holder.itemView.context.getDrawable(R.drawable.custom_progressbar)
                holder.crdProgress.isLongClickable = false
            }

        }

    }

    private fun getNameFirstChar(name: String): String {
        val nameSplit = name.split(" ")
        return nameSplit[nameSplit.size - 1][0].toString().uppercase()
    }
}