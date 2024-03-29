package com.example.baseproject.ui.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.repository.AuthRepository
import com.example.baseproject.utils.Response
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) :
    BaseViewModel() {

    private var _signInResponse = MutableLiveData<Response<Boolean>>()
    val signInResponse: MutableLiveData<Response<Boolean>> get() = _signInResponse
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInResponse.value = Response.Loading
            _signInResponse.value = authRepository.firebaseLogin(email, password)
        }
    }


    private var _isValidEmail = false
    private var _isValidPassword = false


    val isLogin: Boolean
        get() = authRepository.isLogin()
}