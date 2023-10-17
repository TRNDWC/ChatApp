package com.example.baseproject.repositoryImpl


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.FriendState
import com.example.baseproject.model.Profile
import com.example.baseproject.repository.FriendShipsRepository
import com.example.baseproject.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.values


class FriendShipsRepositoryImpl : FriendShipsRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    override fun getAllFriendModel(): MutableLiveData<Response<List<FriendModel>>> {
        val friendListResponse = MutableLiveData<Response<List<FriendModel>>>()
        database.reference.child("users")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val friendList = mutableListOf<FriendModel>()
                    snapshot.children.forEach {
                        if (it.key!!.toString() != auth.uid) {
                            val friend = FriendModel(
                                id = it.key.toString(),
                                name = it.child("profile").child("display_name").value.toString(),
                                profileImage = it.child("profile")
                                    .child("profile_picture").value.toString(),
                                state = FriendState.NONE
                            )
                            friendList.add(friend)
                        }
                    }
                    database.reference.child("friendships").child(auth.uid!!)
                        .addValueEventListener(object :
                            com.google.firebase.database.ValueEventListener {
                            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                                snapshot.children.forEach { friend ->
                                    val friendId = friend.key.toString()
                                    val friendState = friend.value.toString()
                                    friendList.find { it.id == friendId }?.let {
                                        it.state = FriendState.fromString(friendState)
                                    }
                                    friendListResponse.postValue(Response.Success(friendList))
                                }
                            }

                            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                                friendListResponse.postValue(Response.Failure(error.toException()))
                            }
                        })
                    friendListResponse.postValue(Response.Success(friendList))
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    friendListResponse.postValue(Response.Failure(error.toException()))
                }
            })
        return friendListResponse
    }

    override suspend fun updateFriendState(friendId: String, state: String): Response<Boolean> {
        return try {
            when (state) {
                "add" -> {
                    database.reference.child("friendships").child(auth.uid!!).child(friendId)
                        .setValue(FriendState.SENT.toString())
                    database.reference.child("friendships").child(friendId).child(auth.uid!!)
                        .setValue(FriendState.RECEIVED.toString())
                }

                "accept" -> {
                    database.reference.child("friendships").child(auth.uid!!).child(friendId)
                        .setValue(FriendState.FRIEND.toString())
                    database.reference.child("friendships").child(friendId).child(auth.uid!!)
                        .setValue(FriendState.FRIEND.toString())
                }

                else -> {
                    database.reference.child("friendships").child(auth.uid!!).child(friendId)
                        .setValue(FriendState.NONE.toString())
                    database.reference.child("friendships").child(friendId).child(auth.uid!!)
                        .setValue(FriendState.NONE.toString())
                }
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun getNumberOfRequest(): MutableLiveData<Response<Int>> {
        val numberOfRequest = MutableLiveData<Response<Int>>()
        database.reference.child("friendships").child(auth.uid!!)
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    var count = 0
                    snapshot.children.forEach {
                        if (it.value.toString() == FriendState.RECEIVED.toString()) {
                            count++
                        }
                    }
                    numberOfRequest.postValue(Response.Success(count))
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    numberOfRequest.postValue(Response.Failure(error.toException()))
                }
            })
        return numberOfRequest
    }
}