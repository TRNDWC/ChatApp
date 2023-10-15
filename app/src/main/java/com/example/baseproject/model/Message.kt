package com.example.baseproject.model

import java.sql.Time

sealed class MessageType{
    object TEXT : MessageType()
    object IMAGE : MessageType()
    object VIDEO : MessageType()
    object AUDIO : MessageType()
    object FILE : MessageType()
    object LOCATION : MessageType()
    object CONTACT : MessageType()
    object STICKER : MessageType()
    object GIF : MessageType()
    object EMOJI : MessageType()
    object REPLY : MessageType()
    object FORWARD : MessageType()
    object DELETE : MessageType()
    object RECALL : MessageType()
    object POLL : MessageType()
    object DOCUMENT : MessageType()
    object VOICE : MessageType()
    object LINK : MessageType()
    object NONE : MessageType()

    override fun toString(): String {
        return when(this){
            TEXT -> "text"
            IMAGE -> "image"
            VIDEO -> "video"
            AUDIO -> "audio"
            FILE -> "file"
            LOCATION -> "location"
            CONTACT -> "contact"
            STICKER -> "sticker"
            GIF -> "gif"
            EMOJI -> "emoji"
            REPLY -> "reply"
            FORWARD -> "forward"
            DELETE -> "delete"
            RECALL -> "recall"
            POLL -> "poll"
            DOCUMENT -> "document"
            VOICE -> "voice"
            LINK -> "link"
            NONE -> "none"
        }
    }

    companion object{
        fun fromString(string: String): MessageType{
            return when(string){
                "text" -> TEXT
                "image" -> IMAGE
                "video" -> VIDEO
                "audio" -> AUDIO
                "file" -> FILE
                "location" -> LOCATION
                "contact" -> CONTACT
                "sticker" -> STICKER
                "gif" -> GIF
                "emoji" -> EMOJI
                "reply" -> REPLY
                "forward" -> FORWARD
                "delete" -> DELETE
                "recall" -> RECALL
                "poll" -> POLL
                "document" -> DOCUMENT
                "voice" -> VOICE
                "link" -> LINK
                else -> NONE
            }
        }
    }
}

sealed class MessageStatus{
    object SENDING : MessageStatus()
    object SENT : MessageStatus()
    object DELIVERED : MessageStatus()
    object RECEIVED : MessageStatus()
    object FAILED : MessageStatus()
    object NONE : MessageStatus()

    override fun toString(): String {
        return when(this){
            SENDING -> "sending"
            SENT -> "sent"
            DELIVERED -> "delivered"
            RECEIVED -> "received"
            FAILED -> "failed"
            NONE -> "none"
        }
    }

    companion object{
        fun fromString(string: String): MessageStatus{
            return when(string){
                "sending" -> SENDING
                "sent" -> SENT
                "delivered" -> DELIVERED
                "received" -> RECEIVED
                "failed" -> FAILED
                else -> NONE
            }
        }
    }
}

class Message(
    val id: String,
    val senderId: String,
    val roomId : String,
    val content: String,
    val messageTime: Long,
    var messageStatus: MessageStatus,
    val messageType: MessageType,
) {
}