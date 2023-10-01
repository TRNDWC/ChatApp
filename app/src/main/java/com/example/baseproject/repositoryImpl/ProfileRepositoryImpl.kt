package com.example.baseproject.repositoryImpl

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.model.Profile
import com.example.baseproject.repository.ProfileRepository
import com.example.baseproject.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl : ProfileRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun getProfile(): MutableLiveData<Response<Profile>> {
        val profileResponse = MutableLiveData<Response<Profile>>()
        database.reference.child("users").child(auth.uid!!).child("profile")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val profile = Profile(
                        name = snapshot.child("display_name").value.toString(),
                        profilePictureUri = snapshot.child("profile_picture").value.toString(),
                        email = snapshot.child("email").value.toString(),
                        phoneNumber = snapshot.child("phone_number").value.toString(),
                        DOB = snapshot.child("DOB").value.toString()
                    )
                    Log.d("ProfileRepositoryImpl", "onDataChange: ${profile.email} ${profile.name} ${profile.profilePictureUri} ")
                    profileResponse.postValue(Response.Success(profile))
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    profileResponse.postValue(Response.Failure(error.toException()))
                }
            })
        return profileResponse
    }

    override suspend fun updateProfile(
        nName: String?,
        nProfileImage: String?,
        nPhonenum: String?,
        nDOB: String?
    ): Response<Boolean> {
        return try {
            var url: String? = null
            if (nProfileImage != null) {
                val storageRef = storage.reference
                val fileRef = storageRef.child("profile_pictures/${auth.uid}")
                fileRef.putFile(nProfileImage.toUri()).await()
                url = fileRef.downloadUrl.await().toString()
            }
            database.reference.child("users").child(auth.uid!!).child("profile").apply {
                child("display_name").setValue(nName)
                if (url != null) child("profile_picture").setValue(url)
                child("phone_number").setValue(nPhonenum)
                child("DOB").setValue(nDOB)
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


}