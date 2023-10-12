package com.example.baseproject.repository

import com.example.baseproject.model.Profile
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.model.FriendModel
import com.example.baseproject.utils.Response

interface FriendShipsRepository {
    fun getAllFriendModel(): MutableLiveData<Response<List<FriendModel>>>
    suspend fun updateFriendState(friendId: String, state: String): Response<Boolean>
    fun getNumberOfRequest(): MutableLiveData<Response<Int>>
}