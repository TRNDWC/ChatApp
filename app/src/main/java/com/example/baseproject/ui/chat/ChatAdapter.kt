package com.example.baseproject.ui.chat

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.LayoutRoomItemBinding
import com.example.baseproject.model.Message
import com.example.baseproject.model.MessageStatus

class ChatAdapter(
    val mMessage: MutableList<Message>
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var avatar: String? = null

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
        holder.time.text = getTime(message.messageTime)
        holder.time.visibility = ViewGroup.GONE
        if (avatar != null) {
            Glide.with(holder.itemView.context).load(avatar).circleCrop().into(holder.imgAvatar)
        }
        try {
            setUpItem(message.messageStatus, holder)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setAvatar(avatar: String) {
        this.avatar = avatar
    }

    fun setMessages(messages: List<Message>) {
        mMessage.clear()
        mMessage.addAll(messages)
        notifyDataSetChanged()
    }

    fun addMessage(message: List<Message>) {
        mMessage.addAll(0, message)
        notifyDataSetChanged()
    }

    private fun setUpItem(type: MessageStatus, holder: ChatViewHolder) {
        when (type) {
            MessageStatus.SENT -> {
                holder.message.textAlignment = ViewGroup.TEXT_ALIGNMENT_TEXT_END
                holder.message.setBackgroundResource(R.drawable.bg_message_sent_single)
                holder.imgAvatar.visibility = ViewGroup.GONE
                holder.time.visibility = ViewGroup.VISIBLE
                holder.message.setTextColor(Color.WHITE)
                holder.container.gravity = 0x05

                if ((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) {
                    holder.message.setBackgroundResource(R.drawable.bg_message_sent_mid)
                    holder.time.visibility = ViewGroup.GONE
                } else if (((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus != mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) || (holder.adapterPosition == 0 && mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) {
                    holder.message.setBackgroundResource(R.drawable.bg_message_sent_top)
                    holder.time.visibility = ViewGroup.GONE
                } else if (((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus != mMessage[holder.adapterPosition + 1].messageStatus)) || (holder.adapterPosition == mMessage.size - 1 && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus))) {
                    holder.message.setBackgroundResource(R.drawable.bg_message_sent_bottom)
                }
            }

            MessageStatus.RECEIVED -> {
                holder.message.textAlignment = ViewGroup.TEXT_ALIGNMENT_TEXT_START
                holder.message.setBackgroundResource(R.drawable.bg_message_received_single)
                holder.imgAvatar.visibility = ViewGroup.VISIBLE
                holder.time.visibility = ViewGroup.VISIBLE
                holder.message.setTextColor(Color.BLACK)
                holder.container.gravity = 0x03

                if ((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) {
                    holder.message.setBackgroundResource(R.drawable.bg_message_received_mid)
                    holder.imgAvatar.visibility = ViewGroup.GONE
                    holder.time.visibility = ViewGroup.GONE
                } else if (((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus != mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) || (holder.adapterPosition == 0 && mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) {
                    holder.message.setBackgroundResource(R.drawable.bg_message_received_top)
                    holder.imgAvatar.visibility = ViewGroup.GONE
                    holder.time.visibility = ViewGroup.GONE
                } else if (((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus != mMessage[holder.adapterPosition + 1].messageStatus)) || (holder.adapterPosition == mMessage.size - 1 && mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus)) {
                    holder.message.setBackgroundResource(R.drawable.bg_message_received_bottom)
                }
            }
        }
    }

    private fun getTime(s: Long): String? {
        return try {
//            lấy giờ phút và ngày
            val sdf = java.text.SimpleDateFormat("HH:mm-dd/MM")
            val netDate = java.util.Date(s)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}