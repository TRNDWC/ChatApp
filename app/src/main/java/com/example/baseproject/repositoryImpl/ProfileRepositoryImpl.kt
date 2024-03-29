package com.example.baseproject.repositoryImpl

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.FriendState
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
                        uid = auth.uid!!,
                        name = snapshot.child("display_name").value.toString(),
                        profilePictureUri = snapshot.child("profile_picture").value.toString(),
                        email = snapshot.child("email").value.toString(),
                        phoneNumber = snapshot.child("phone_number").value.toString(),
                        DOB = snapshot.child("DOB").value.toString()
                    )
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
            Log.d("ProfileRepositoryImpl", "updateProfile: $nName $nProfileImage $nPhonenum $nDOB")
            var url: String? = null
            if (nProfileImage != null) {
                val storageRef = storage.reference
                val fileRef = storageRef.child("profile_pictures/${auth.uid}")
                fileRef.putFile(nProfileImage.toUri()).await()
                url = fileRef.downloadUrl.await().toString()
            }

            if (nProfileImage != null) {
                Log.d("ProfileRepositoryImpl", "updateProfile: ${nProfileImage.toUri()}")
            }
            database.reference.child("users").child(auth.uid!!).child("profile").apply {
                if (nName != "") child("display_name").setValue(nName)
                if (url != null) child("profile_picture").setValue(url)
                if (nPhonenum != "") child("phone_number").setValue(nPhonenum)
                if (nDOB != "") child("DOB").setValue(nDOB)
                child("uid").setValue(auth.uid!!)
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun getFriend(id: String): MutableLiveData<Response<FriendModel>> {
        val friendResponse = MutableLiveData<Response<FriendModel>>()
        database.reference.child("users").child(id).child("profile")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val friend = FriendModel(
                        id = id,
                        name = snapshot.child("display_name").value.toString(),
                        profileImage = snapshot.child("profile_picture").value.toString(),
                        state = FriendState.FRIEND
                    )
                    friendResponse.postValue(Response.Success(friend))
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    friendResponse.postValue(Response.Failure(error.toException()))
                }
            })
        return friendResponse
    }
}