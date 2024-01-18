package com.example.baseproject.ui.message

import androidx.lifecycle.ViewModel
import com.example.baseproject.repository.ChatRepository
import com.example.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val repository: ChatRepository
) : BaseViewModel() {

    val message = repository.getAllChat()

}