package com.example.baseproject.model

enum class FriendState {
    FRIEND,
    SENT,
    RECEIVED,
    NONE;

    override fun toString(): String {
        return when (this) {
            FRIEND -> "friend"
            SENT -> "request_sent"
            RECEIVED -> "request_received"
            NONE -> "none"
        }
    }

    companion object {
        fun fromString(string: String): FriendState {
            return when (string) {
                "friend" -> FRIEND
                "request_sent" -> SENT
                "request_received" -> RECEIVED
                else -> NONE
            }
        }
    }
}

class FriendModel(
    val id: String,
    val name: String,
    val profileImage: String?,
    var state: FriendState
)