package com.example.baseproject.extension

import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.baseproject.R
import com.example.baseproject.model.Message
import com.example.baseproject.model.MessageStatus
import com.example.baseproject.ui.chat.ChatAdapter

fun ChatAdapter.ChatViewHolder.setPosition(type: MessageStatus, mMessage: MutableList<com.example.baseproject.model.Message>) {
    val holder = this
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

fun ChatAdapter.ChatViewHolder.setImgContent(message: Message, mMessage: MutableList<Message>) {
    val holder = this
    Log.d("ChatAdapter", "onBindViewHolder: ${message.content}")
    holder.imgContent.visibility = ViewGroup.VISIBLE
    holder.message.visibility = ViewGroup.GONE
    Glide.with(holder.itemView.context).load(message.content).placeholder(R.drawable.img_progress).into(holder.imgContent)
    if (message.messageStatus == MessageStatus.SENT) {
        holder.container.gravity = 0x05
        holder.imgContent.scaleType = ImageView.ScaleType.FIT_END
        holder.imgAvatar.visibility = ViewGroup.GONE
    } else {
        holder.container.gravity = 0x03
        holder.imgContent.scaleType = ImageView.ScaleType.FIT_START
        holder.imgAvatar.visibility = ViewGroup.VISIBLE
        if ((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) {
            holder.imgAvatar.visibility = ViewGroup.GONE
            holder.time.visibility = ViewGroup.GONE
        } else if (((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus != mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) || (holder.adapterPosition == 0 && mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) {
            holder.imgAvatar.visibility = ViewGroup.GONE
            holder.time.visibility = ViewGroup.GONE
        } else if (((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus != mMessage[holder.adapterPosition + 1].messageStatus)) || (holder.adapterPosition == mMessage.size - 1 && mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus)) {
            holder.imgAvatar.visibility = ViewGroup.VISIBLE
            holder.time.visibility = ViewGroup.VISIBLE
        }
    }
}

fun ChatAdapter.ChatViewHolder.setEmojiContent(message: Message, mMessage: MutableList<Message>) {
    val holder = this
    holder.imgContent.visibility = ViewGroup.VISIBLE
    holder.message.visibility = ViewGroup.GONE
    Glide.with(holder.itemView.context).load(message.content).placeholder(R.drawable.img_progress).into(holder.imgContent)
    if (message.messageStatus == MessageStatus.SENT) {
        holder.container.gravity = 0x05
        holder.imgContent.scaleType = ImageView.ScaleType.FIT_END
        holder.imgAvatar.visibility = ViewGroup.GONE
    } else {
        holder.container.gravity = 0x03
        holder.imgContent.scaleType = ImageView.ScaleType.FIT_START
        holder.imgAvatar.visibility = ViewGroup.VISIBLE
        if ((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) {
            holder.imgAvatar.visibility = ViewGroup.GONE
            holder.time.visibility = ViewGroup.GONE
        } else if (((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus != mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) || (holder.adapterPosition == 0 && mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition + 1].messageStatus)) {
            holder.imgAvatar.visibility = ViewGroup.GONE
            holder.time.visibility = ViewGroup.GONE
        } else if (((holder.adapterPosition > 0 && holder.adapterPosition < mMessage.size - 1) && (mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus) && (mMessage[holder.adapterPosition].messageStatus != mMessage[holder.adapterPosition + 1].messageStatus)) || (holder.adapterPosition == mMessage.size - 1 && mMessage[holder.adapterPosition].messageStatus == mMessage[holder.adapterPosition - 1].messageStatus)) {
            holder.imgAvatar.visibility = ViewGroup.VISIBLE
            holder.time.visibility = ViewGroup.VISIBLE
        }
    }
}