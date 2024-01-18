package com.example.baseproject.ui.chat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.databinding.LayoutRoomItemBinding
import com.example.baseproject.extension.setEmojiContent
import com.example.baseproject.extension.setImgContent
import com.example.baseproject.extension.setPosition
import com.example.baseproject.model.Message
import com.example.baseproject.model.MessageStatus
import com.example.baseproject.model.MessageType
import com.example.core.utils.toast

interface OnClickItemListener {
    fun onClickItem(view: LayoutRoomItemBinding, position: Int)
}

class ChatAdapter(
    val mMessage: MutableList<Message>,
    val onClickItemListener: OnClickItemListener
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var avatar: String? = null

    inner class ChatViewHolder(
        mMessageItem: LayoutRoomItemBinding,
        onClickItemListener: OnClickItemListener
    ) : RecyclerView.ViewHolder(mMessageItem.root) {
        val message = mMessageItem.tvContent
        val time = mMessageItem.tvTime
        val imgAvatar = mMessageItem.imgAvatar
        val container = mMessageItem.roomItem
        val imgContent = mMessageItem.imgPhoto

        init {
            mMessageItem.apply {
                container.setOnClickListener {
                    onClickItemListener.onClickItem(mMessageItem, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val mMessageItem: LayoutRoomItemBinding = LayoutRoomItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChatViewHolder(mMessageItem, onClickItemListener)
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
        if (message.messageType == MessageType.IMAGE) {
            holder.setImgContent(message, mMessage)

        } else if (message.messageType == MessageType.EMOJI) {
            holder.setEmojiContent(message, mMessage)
        } else {
            holder.imgContent.visibility = ViewGroup.GONE
            try {
                setUpItem(message.messageStatus, holder)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setAvatar(avatar: String) {
        this.avatar = avatar
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMessages(messages: List<Message>) {
        if (mMessage.isEmpty()) {
            mMessage.addAll(messages)
            notifyDataSetChanged()
        } else {
            val lastMessage = mMessage.last()
            val lastMessageTime = lastMessage.messageTime
            val newMessages = messages.filter { it.messageTime > lastMessageTime }
            val oldestMessage = mMessage.first()
            val oldestMessageTime = oldestMessage.messageTime
            val oldMessages = messages.filter { it.messageTime < oldestMessageTime }
            mMessage.addAll(0, oldMessages)
            mMessage.addAll(newMessages)
            notifyDataSetChanged()
        }
    }

    private fun setUpItem(type: MessageStatus, holder: ChatViewHolder) {
        holder.message.visibility = ViewGroup.VISIBLE
        holder.setPosition(type, mMessage)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTime(s: Long): String? {
        return try {
            val sdf = java.text.SimpleDateFormat("HH:mm")
            val netDate = java.util.Date(s)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}