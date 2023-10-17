package com.example.baseproject.ui.authentication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.baseproject.repository.AuthRepository
import com.example.baseproject.utils.Response
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {
    private var _signUpResponse: MutableLiveData<Response<Boolean>> = MutableLiveData()
    val signUpResponse: MutableLiveData<Response<Boolean>> get() = _signUpResponse

    private var _validator: MutableLiveData<Boolean> = MutableLiveData()
    val validator: MutableLiveData<Boolean> get() = _validator

    fun signUp(email: String, password: String, displayName: String) {
        Log.e("SignupViewModel", "signUp: $email $password $displayName")
        viewModelScope.launch {
            _signUpResponse.value = Response.Loading
            _signUpResponse.value = authRepository.firebaseSignUp(email, password, displayName)
        }
    }

    fun setValidState(
        isValidEmail: Boolean = false,
        isValidPassword: Boolean = false,
        isValidUserName: Boolean = false) {
        _validator.value = isValidEmail && isValidPassword && isValidUserName
    }
}