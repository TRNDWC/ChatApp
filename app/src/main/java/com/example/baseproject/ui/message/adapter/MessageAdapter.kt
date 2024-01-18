package com.example.baseproject.ui.message.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.databinding.LayoutMessageItemBinding
import com.example.baseproject.model.Chat
import com.example.baseproject.model.MessageStatus
import com.example.baseproject.model.MessageType
import com.example.baseproject.model.Profile

interface OnMessageItemClicked {
    fun onMessageItemClicked(chat: Chat)
}

class MessageAdapter(
    private val mMessageList: List<Chat>,
    private val onItemClickListener: OnMessageItemClicked
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(
        mMessageItem: LayoutMessageItemBinding, onItemClickListener: OnMessageItemClicked
    ) : RecyclerView.ViewHolder(mMessageItem.root) {
        val messageName = mMessageItem.txtName
        val messageImage = mMessageItem.imgAvatar
        val messageText = mMessageItem.txtMessage
        val icNotice = mMessageItem.imgNotice
        val timeText = mMessageItem.txtTime

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = mMessageList[position]
        holder.messageName.text = item.name
        if (item.image != null) {
            Glide.with(holder.itemView.context)
                .load(item.image.toUri())
                .circleCrop()
                .into(holder.messageImage)
        } else {
            holder.messageImage.setImageResource(R.drawable.ic_profile)
        }
        if (item.lastMessage?.messageType == MessageType.IMAGE) {
            if (item.lastMessage.messageStatus == MessageStatus.RECEIVED) {
                holder.messageText.text = item.name + ": " + holder.itemView.context.getString(R.string.you_sent_an_image)
            } else {
                holder.messageText.text = holder.itemView.context.getString(R.string.you) + ": " + holder.itemView.context.getString(R.string.you_sent_an_image)
            }
            holder.messageText.setTypeface(null, Typeface.ITALIC)
        } else if(item.lastMessage?.messageType == MessageType.EMOJI){
            if (item.lastMessage.messageStatus == MessageStatus.RECEIVED) {
                holder.messageText.text = item.name + ": " + holder.itemView.context.getString(R.string.you_sent_an_emoji)
            } else {
                holder.messageText.text = holder.itemView.context.getString(R.string.you) + ": " + holder.itemView.context.getString(R.string.you_sent_an_emoji)
            }
            holder.messageText.setTypeface(null, Typeface.ITALIC)
        }
        else {
            if (item.lastMessage?.messageStatus == MessageStatus.RECEIVED) {
                holder.messageText.text = item.name + ": " + item.lastMessage.content
            } else {
                holder.messageText.text = holder.itemView.context.getString(R.string.you) + ": " + item.lastMessage?.content
            }
            holder.messageText.setTypeface(null, Typeface.NORMAL)
        }
        holder.timeText.text = getTime(item.lastMessage?.messageTime!!)
    }

    private fun getTime(s: Long): String? {
        return try {
            val sdf = java.text.SimpleDateFormat("HH:mm-dd/MM")
            val netDate = java.util.Date(s)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }
}