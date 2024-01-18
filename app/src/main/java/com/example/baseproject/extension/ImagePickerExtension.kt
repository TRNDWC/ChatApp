package com.example.baseproject.extension

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.github.dhaval2404.imagepicker.ImagePicker

fun Activity.pickImageFromGallery(startForProfileImageResult: ActivityResultLauncher<Intent>) {
    ImagePicker.with(this)
        .galleryOnly()
        .compress(1024)
        .maxResultSize(1080, 1080)
        .crop(12f, 16f)
        .createIntent { intent ->
            startForProfileImageResult.launch(intent)
        }
}