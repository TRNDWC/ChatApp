package com.example.baseproject.ui.chat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.baseproject.R
import com.example.baseproject.databinding.LayoutMessageItemBinding
import com.example.baseproject.databinding.LayoutRoomItemBinding
import com.example.baseproject.databinding.LayoutRoomItemBindingImpl
import com.example.baseproject.model.Message
import com.example.baseproject.model.MessageStatus

class ChatAdapter(
    private val mMessage: List<Message>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    inner class ChatViewHolder(
        mMessageItem: LayoutRoomItemBinding
    ) : RecyclerView.ViewHolder(mMessageItem.root) {
        val message = mMessageItem.tvContent
        val time = mMessageItem.tvTime
        val imgAvatar = mMessageItem.imgAvatar
        val container = mMessageItem.roomItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val mMessageItem: LayoutRoomItemBinding = LayoutRoomItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChatViewHolder(mMessageItem)
    }

    override fun getItemCount(): Int {
        return mMessage.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = mMessage[position]
        holder.message.text = message.content
        holder.time.text = message.messageTime.toString()
        holder.imgAvatar.setImageResource(R.drawable.ic_profile)
        if (message.messageStatus== MessageStatus.RECEIVED) {
            holder.imgAvatar.visibility = ViewGroup.VISIBLE
            holder.message.setBackgroundResource(R.drawable.bg_message_received)
            holder.message.setTextColor(Color.BLACK)
            holder.message.textAlignment = ViewGroup.TEXT_ALIGNMENT_TEXT_START
            holder.container.gravity = 0x03
        } else {
            holder.imgAvatar.visibility = ViewGroup.GONE
            holder.message.setBackgroundResource(R.drawable.bg_message_sent)
            holder.message.setTextColor(Color.WHITE)
            holder.message.textAlignment = ViewGroup.TEXT_ALIGNMENT_TEXT_END
            holder.container.gravity = 0x05
        }
    }
}