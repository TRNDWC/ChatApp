package com.example.baseproject.ui.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.Chat
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.Message
import com.example.baseproject.repository.ChatRepository
import com.example.baseproject.repository.ProfileRepository
import com.example.baseproject.utils.Response
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    val mChat = MutableLiveData<Chat?>()
    val mMessages = MutableLiveData<Response<List<Message>>>()
    val loadMessages = MutableLiveData<Response<List<Message>>>()
    val mFriend = MutableLiveData<Response<FriendModel>>()
    fun getFriend(id: String) {

        val response = profileRepository.getFriend(id)
        response.observeForever {
            mFriend.postValue(it)
        }
    }

    fun sendMessageTo(message: Message, friend: FriendModel) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.sendMessageTo(message, friend)
        }
    }

    fun readMessage(friend: FriendModel) {
        val response = chatRepository.readMessage(friend, null)
        response.observeForever {
            mMessages.postValue(it)
        }
    }

    fun loadMoreMessages(friend: FriendModel, lastMessage: Message?) {
        val response = chatRepository.readMessage(friend, lastMessage)
        response.observeForever {
            loadMessages.postValue(it)
        }
    }

    fun deleteChat(friend: FriendModel) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.deleteChat(friend)
        }
    }
}