package com.example.baseproject.repository

import androidx.lifecycle.MutableLiveData
import com.example.baseproject.model.Profile
import com.example.baseproject.utils.Response
import com.google.firebase.storage.StorageReference

interface ProfileRepository {
    fun getProfile(): MutableLiveData<Response<Profile>>
    suspend fun updateProfile(
        nName: String?,
        nProfileImage: String?,
        nPhonenum: String?,
        nDOB: String?
    ): Response<Boolean>

}