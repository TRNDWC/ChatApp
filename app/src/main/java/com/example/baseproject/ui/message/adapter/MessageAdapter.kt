package com.example.baseproject.ui.message.adapter

import android.graphics.Typeface
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.databinding.LayoutMessageItemBinding
import com.example.baseproject.model.Profile

interface OnMessageItemClicked {
    fun onMessageItemClicked(profile: Profile)
}

class MessageAdapter(
    private val mMessageList: List<Profile>,
    private val onItemClickListener: OnMessageItemClicked
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>(){

    inner class MessageViewHolder(
        mMessageItem: LayoutMessageItemBinding, onItemClickListener: OnMessageItemClicked
    ) : RecyclerView.ViewHolder(mMessageItem.root) {
        val messageName = mMessageItem.txtName
        val messageImage = mMessageItem.imgAvatar
        val messageText = mMessageItem.txtMessage
        val icNotice = mMessageItem.imgNotice

        init {
            mMessageItem.apply {
                messageItem.setOnClickListener {
                    onItemClickListener.onMessageItemClicked(
                        mMessageList[adapterPosition]
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val mMessageItem: LayoutMessageItemBinding = LayoutMessageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MessageViewHolder(mMessageItem, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return mMessageList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = mMessageList[position]

        holder.messageName.text = item.name

        if (position%2==0){
            holder.messageImage.background = holder.itemView.context.getDrawable(R.drawable.round_avatar_no_border)
            holder.messageName.typeface = Typeface.DEFAULT
            holder.messageText.typeface = Typeface.DEFAULT
            holder.icNotice.visibility = android.view.View.GONE
        } else {
            holder.messageImage.background = holder.itemView.context.getDrawable(R.drawable.round_avatar_border)
            holder.messageName.typeface = Typeface.DEFAULT_BOLD
            holder.messageText.typeface = Typeface.DEFAULT_BOLD
            holder.icNotice.visibility = android.view.View.VISIBLE
        }

        if (item.profilePictureUri != null) {
            holder.messageImage.setImageURI(item.profilePictureUri.toUri())
        } else {
            holder.messageImage.setImageResource(R.drawable.ic_default_user)
        }
    }

}