package com.example.baseproject.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.Profile
import com.example.baseproject.repository.AuthRepository
import com.example.baseproject.repository.ProfileRepository
import com.example.baseproject.utils.Response
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
) :
    BaseViewModel() {

    private var _mProfile = profileRepository.getProfile()
    val mProfile: LiveData<Response<Profile>> get() = _mProfile

    private var _logOutResponse = MutableLiveData<Response<Boolean>>()
    val logOutResponse: MutableLiveData<Response<Boolean>> get() = _logOutResponse

    fun logOut() {
        viewModelScope.launch {
            _logOutResponse.value = Response.Loading
            _logOutResponse.value = authRepository.firebaseLogout()
        }
    }

    var nAvatar = MutableLiveData<String>()

    fun updateProfile(profile: Profile) {
        viewModelScope.launch {
            profileRepository.updateProfile(profile.name, profile.profilePictureUri, profile.phoneNumber, profile.DOB)
        }
        Log.d("ProfileViewModel", "updateProfile: ${profile.name} ${profile.profilePictureUri}")
    }

}