package com.example.baseproject.ui.friend.adpater

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.LayoutFriendsItemBinding
import com.example.baseproject.model.FriendModel

class SearchFriendAdapter(
    private var mFriendList: List<FriendModel>, private val onItemClickListener: OnFriendItemClicked
) : RecyclerView.Adapter<SearchFriendAdapter.SearchFriendViewHolder>() {

    inner class SearchFriendViewHolder(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFriendViewHolder {
        val mFriendTabItem: LayoutFriendsItemBinding = LayoutFriendsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SearchFriendViewHolder(mFriendTabItem, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return mFriendList.size
    }

    override fun onBindViewHolder(holder: SearchFriendViewHolder, position: Int) {
        val item = mFriendList[position]
        holder.friendFirstChar.visibility = ViewGroup.GONE
        holder.friendName.text = item.name
        if (item.profileImage.isNullOrEmpty()) {
            holder.friendImage.setImageResource(R.drawable.ic_profile)
        } else
            Glide.with(holder.itemView.context)
                .load(item.profileImage.toUri())
                .circleCrop()
                .into(holder.friendImage)
    }

    fun setFiltedList(list: List<FriendModel>) {
        mFriendList = list
        notifyDataSetChanged()
    }
}