package com.example.baseproject.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.model.Chat
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.Message
import com.example.baseproject.utils.Response

interface ChatRepository {
    suspend fun getChatByFriend(friend: FriendModel): Response<Chat>
    suspend fun setChatByFriend(friend: FriendModel): Response<Boolean>
    suspend fun sendMessageTo(message: Message, friend: FriendModel): Response<Boolean>
    suspend fun sendImageMessageTo(message: Uri, friend: FriendModel): Response<Boolean>

    suspend fun sendMemeTo(meme: String, friend: FriendModel): Response<Boolean>

    fun lastMessage(friend: FriendModel): MutableLiveData<Response<Message?>>
    suspend fun deleteChat(friend: FriendModel): Response<Boolean>
    fun readMessage(
        friend: FriendModel, lastMessage: Message?
    ): MutableLiveData<Response<List<Message>>>

    fun getAllChat(): MutableLiveData<Response<List<Chat>>>

}