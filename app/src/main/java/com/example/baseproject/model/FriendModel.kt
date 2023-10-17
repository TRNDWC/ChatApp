package com.example.baseproject.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

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
    val id: String?,
    val name: String?,
    val profileImage: String?,
    var state: FriendState
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        FriendState.fromString(parcel.readString()!!)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(profileImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FriendModel> {
        override fun createFromParcel(parcel: Parcel): FriendModel {
            return FriendModel(parcel)
        }

        override fun newArray(size: Int): Array<FriendModel?> {
            return arrayOfNulls(size)
        }
    }

}