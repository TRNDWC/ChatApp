package com.example.baseproject.ui.image_gallery

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.model.Image
import com.example.core.base.BaseViewModel
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageGalleryViewModel  @Inject constructor(
    private val application: Application,
): BaseViewModel() {


}