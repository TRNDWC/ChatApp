package com.example.baseproject.ui.friend.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.LayoutFriendsItemBinding
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.FriendState
import com.example.baseproject.model.Profile

interface OnFriendItemClicked {
    fun onFriendItemClicked(friend: FriendModel)
}

class FriendTabAdapter(
    private var mFriendList: List<FriendModel>, private val onItemClickListener: OnFriendItemClicked
) : RecyclerView.Adapter<FriendTabAdapter.FriendTabViewHolder>() {

    inner class FriendTabViewHolder(
        mFriendTabItem: LayoutFriendsItemBinding, onItemClickListener: OnFriendItemClicked
    ) : RecyclerView.ViewHolder(mFriendTabItem.root) {
        val friendName = mFriendTabItem.txtName
        val friendImage = mFriendTabItem.imgAvatar
        val friendFirstChar = mFriendTabItem.txtChar

        init {
            mFriendTabItem.apply {
                friendsItem.setOnClickListener {
                    onItemClickListener.onFriendItemClicked(
                        mFriendList[adapterPosition]
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendTabViewHolder {
        val mFriendTabItem: LayoutFriendsItemBinding = LayoutFriendsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FriendTabViewHolder(mFriendTabItem, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return mFriendList.size
    }

    override fun onBindViewHolder(holder: FriendTabViewHolder, position: Int) {
        val item = mFriendList[position]
        if (position == 0 ||
            getNameFirstChar(mFriendList[position].name!!) != getNameFirstChar(mFriendList[position - 1].name!!)
        ) {
            holder.friendFirstChar.visibility = ViewGroup.VISIBLE
            holder.friendFirstChar.text = getNameFirstChar(item.name!!)
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
    }

    private fun getNameFirstChar(name: String): String {
        val nameSplit = name.split(" ")
        return nameSplit[nameSplit.size - 1][0].toString().uppercase()
    }
}