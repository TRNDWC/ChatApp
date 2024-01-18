package com.example.baseproject.ui.chat

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.api.ApiService
import com.example.baseproject.model.Chat
import com.example.baseproject.model.Data
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.Image
import com.example.baseproject.model.Meme
import com.example.baseproject.model.Message
import com.example.baseproject.repository.ChatRepository
import com.example.baseproject.repository.ProfileRepository
import com.example.baseproject.utils.Response
import com.example.core.base.BaseViewModel
import com.example.core.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository,
    private val application: Application
) : BaseViewModel() {

    val mChat = MutableLiveData<Chat?>()
    val mMessages = MutableLiveData<Response<List<Message>>>()
    val loadMessages = MutableLiveData<Response<List<Message>>>()
    val mFriend = MutableLiveData<Response<FriendModel>>()
    val imgMessages = MutableLiveData<List<Image>>()

    fun getFriend(id: String) {
        val response = profileRepository.getFriend(id)
        response.observeForever {
            mFriend.postValue(it)
        }
    }

    init {
        imgMessages.value = mutableListOf()
    }

    fun sendMessageTo(message: Message, friend: FriendModel) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.sendMessageTo(message, friend)
        }
    }

    fun sendImageMessageTo(message: Uri, friend: FriendModel) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.sendImageMessageTo(message, friend)
            Log.d("TAG", "sendImageMessageTo: ")
        }
    }

    fun sendMemeTo(meme: String, friend: FriendModel) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.sendMemeTo(meme, friend)
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

    val listImage = MutableLiveData<MutableList<Image>>()

    @SuppressLint("Range")
    fun getAllImageFromExternalStorage() {
        val list = mutableListOf<Image>()
        val contentResolver = application.contentResolver
        val uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val path = cursor.getString(cursor.getColumnIndex(android.provider.MediaStore.Images.Media.DATA))
                val image = Image()
                image.url = "file://$path"
                list.add(image)
            }
            cursor.close()
        }
        listImage.value = list
    }

    val listMemes = MutableLiveData<List<Meme>>()

    fun getMemes() {
        ApiService.create().getMemes().enqueue(object : retrofit2.Callback<Data> {
            override fun onResponse(call: retrofit2.Call<Data>, response: retrofit2.Response<Data>) {
                if (response.isSuccessful) {
                    val Data = response.body()
                    if (Data != null) {
                        listMemes.value = Data.data.memes
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<Data>, t: Throwable) {
            }
        })
    }
}