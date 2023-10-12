package com.example.baseproject.ui.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.Profile
import com.example.baseproject.repository.FriendShipsRepository
import com.example.baseproject.repository.ProfileRepository
import com.example.baseproject.utils.Response
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val friendRepository: FriendShipsRepository,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {
    var mFriendModelList = friendRepository.getAllFriendModel()
    var numberOfResquest = friendRepository.getNumberOfRequest()
    var isBackFromSearch = MutableLiveData<Boolean>(false)
    var query = MutableLiveData<String>("")

    fun addFriend(friendId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            friendRepository.updateFriendState(friendId, "add")
        }
    }
    fun acceptFriend(friendId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            friendRepository.updateFriendState(friendId, "accept")
        }
    }

    fun rejectFriend(friendId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            friendRepository.updateFriendState(friendId, "reject")
        }
    }
}