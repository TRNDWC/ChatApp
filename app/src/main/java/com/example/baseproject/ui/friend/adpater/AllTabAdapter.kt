package com.example.baseproject.ui.friend.adpater

import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.databinding.LayoutAllItemBinding
import com.example.baseproject.model.Profile

interface OnAllItemClickListener {
    fun onAllItemClicked(binding: LayoutAllItemBinding, profile: Profile)
    fun onAllItemLongClicked(binding: LayoutAllItemBinding, profile: Profile)
}

class AllTabAdapter(
    private var mProfileList: List<Profile>,
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
        if (item.profilePictureUri != null) {
            holder.friendImage.setImageURI(item.profilePictureUri.toUri())
        } else {
            holder.friendImage.setImageResource(R.drawable.ic_launcher_foreground)
        }
        if (position % 2 == 0) {
            holder.crdProgress.visibility = ViewGroup.GONE
        } else {
            holder.crdProgress.visibility = ViewGroup.VISIBLE
            holder.crdProgress.isLongClickable = false
            holder.barProgress.progressDrawable =
                holder.itemView.context.getDrawable(R.drawable.custom_progressbar)
            holder.btnAddFriend.text =
                holder.itemView.context.getString(R.string.add_friend)
            holder.btnAddFriend.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

    private fun getNameFirstChar(name: String): String {
        val nameSplit = name.split(" ")
        return nameSplit[nameSplit.size - 1][0].toString().uppercase()
    }
}