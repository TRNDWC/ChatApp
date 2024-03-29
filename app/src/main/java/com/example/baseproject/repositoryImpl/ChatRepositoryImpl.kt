package com.example.baseproject.repositoryImpl

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.example.baseproject.model.Chat
import com.example.baseproject.model.FriendModel
import com.example.baseproject.model.FriendState
import com.example.baseproject.model.Message
import com.example.baseproject.model.MessageStatus
import com.example.baseproject.model.MessageType
import com.example.baseproject.repository.ChatRepository
import com.example.baseproject.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatRepositoryImpl : ChatRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val coroutineScope = kotlinx.coroutines.CoroutineScope(
        kotlinx.coroutines.Dispatchers.IO
    )

    override suspend fun getChatByFriend(friend: FriendModel): Response<Chat> {
        val chatId = if (auth.uid!! > friend.id!!) {
            "${auth.uid}-${friend.id}"
        } else {
            "${friend.id}-${auth.uid}"
        }
        val friendId = chatId.replace(auth.uid!!, "").replace("-", "")
        val chatRef = database.getReference("chats/$chatId")
        val chat = MutableLiveData<Chat?>()
        chatRef.get().addOnSuccessListener {
            if (it.exists()) {
                val chatId = it.key as String
                val id = it.child("id").value as String
                val name = if (it.child("name").exists()) {
                    friend.name
                } else {
                    it.child("name").value as String
                }
                val image = if (it.child("image").exists()) {
                    friend.profileImage
                } else {
                    it.child("image").value as String
                }
                val lastMessage = if (it.child("lastMessage").exists()) {
                    val id = it.child("lastMessage/id").value as String
                    val senderId = it.child("lastMessage/senderId").value as String
                    val content = it.child("lastMessage/content").value as String
                    val type = MessageType.fromString(it.child("lastMessage/type").value as String)
                    val status = if (auth.uid == senderId) {
                        MessageStatus.SENT
                    } else {
                        MessageStatus.RECEIVED
                    }
                    val time = it.child("lastMessage/time").value as Long
                    val message = Message(id, senderId, chatId, content, status, type, time)
                    message
                } else {
                    null
                }
                val mChat = Chat(id, friendId, name, image, lastMessage)
                chat.value = mChat
            }
        }.await()
        return Response.Success(chat.value!!)
    }

    override suspend fun setChatByFriend(friend: FriendModel): Response<Boolean> {
        Log.d("trndwcs", "this function is called")
        val chatId = if (auth.uid!! > friend.id!!) {
            "${auth.uid}-${friend.id}"
        } else {
            "${friend.id}-${auth.uid}"
        }
        return try {
            val chatRef = database.reference.child("chats").child(chatId)
            val chat = Chat(chatId, null, null, null, null)
            chatRef.setValue(chat).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendMessageTo(message: Message, friend: FriendModel): Response<Boolean> {
        val chatId = if (auth.uid!! > friend.id!!) {
            "${auth.uid}-${friend.id}"
        } else {
            "${friend.id}-${auth.uid}"
        }
        return try {
            val chatRef = database.reference.child("chats").child(chatId)
            val messageRef = database.reference.child("messages").child(chatId)
            val messageId = messageRef.push().key!!
            val message = Message(
                messageId,
                auth.uid!!,
                chatId,
                message.content,
                MessageStatus.SENT,
                MessageType.TEXT,
                System.currentTimeMillis()
            )
            messageRef.child(messageId).apply {
                child("id").setValue(messageId)
                child("senderId").setValue(auth.uid)
                child("receivedId").setValue(friend.id)
                child("content").setValue(message.content)
                child("type").setValue(message.messageType.toString())
                child("time").setValue(message.messageTime)
            }
            chatRef.child("lastMessage").apply {
                child("id").setValue(messageId)
                child("senderId").setValue(auth.uid)
                child("receivedId").setValue(friend.id)
                child("content").setValue(message.content)
                child("type").setValue(message.messageType.toString())
                child("time").setValue(message.messageTime)
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendImageMessageTo(
        message: Uri,
        friend: FriendModel
    ): Response<Boolean> {
        val chatId = if (auth.uid!! > friend.id!!) {
            "${auth.uid}-${friend.id}"
        } else {
            "${friend.id}-${auth.uid}"
        }

        Log.d("TAG", "sendImageMessageTo: $message")

        return try {

            var url: String? = null
            if (message != null) {
                val storageRef = storage.reference
                val fileRef = storageRef.child("messages")
                fileRef.putFile(message).await()
                url = fileRef.downloadUrl.await().toString()
            }

            Log.d("TAG", "sendImageMessageTo: $message")

            val chatRef = database.reference.child("chats").child(chatId)
            val messageRef = database.reference.child("messages").child(chatId)
            val messageId = messageRef.push().key!!
            if (url != null) {
                val message = Message(
                    messageId,
                    auth.uid!!,
                    chatId,
                    url,
                    MessageStatus.SENT,
                    MessageType.IMAGE,
                    System.currentTimeMillis()
                )
                messageRef.child(messageId).apply {
                    child("id").setValue(messageId)
                    child("senderId").setValue(auth.uid)
                    child("receivedId").setValue(friend.id)
                    child("content").setValue(message.content)
                    child("type").setValue(message.messageType.toString())
                    child("time").setValue(message.messageTime)
                }
                chatRef.child("lastMessage").apply {
                    child("id").setValue(messageId)
                    child("senderId").setValue(auth.uid)
                    child("receivedId").setValue(friend.id)
                    child("content").setValue(message.content)
                    child("type").setValue(message.messageType.toString())
                    child("time").setValue(message.messageTime)
                }
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendMemeTo(meme: String, friend: FriendModel): Response<Boolean> {
val chatId = if (auth.uid!! > friend.id!!) {
            "${auth.uid}-${friend.id}"
        } else {
            "${friend.id}-${auth.uid}"
        }
        return try {
            val chatRef = database.reference.child("chats").child(chatId)
            val messageRef = database.reference.child("messages").child(chatId)
            val messageId = messageRef.push().key!!
            val message = Message(
                messageId,
                auth.uid!!,
                chatId,
                meme,
                MessageStatus.SENT,
                MessageType.EMOJI,
                System.currentTimeMillis()
            )
            messageRef.child(messageId).apply {
                child("id").setValue(messageId)
                child("senderId").setValue(auth.uid)
                child("receivedId").setValue(friend.id)
                child("content").setValue(message.content)
                child("type").setValue(message.messageType.toString())
                child("time").setValue(message.messageTime)
            }
            chatRef.child("lastMessage").apply {
                child("id").setValue(messageId)
                child("senderId").setValue(auth.uid)
                child("receivedId").setValue(friend.id)
                child("content").setValue(message.content)
                child("type").setValue(message.messageType.toString())
                child("time").setValue(message.messageTime)
            }
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


    override fun lastMessage(friend: FriendModel): MutableLiveData<Response<Message?>> {
        val chatId = if (auth.uid!! > friend.id!!) {
            "${auth.uid}-${friend.id}"
        } else {
            "${friend.id}-${auth.uid}"
        }
//        realtime update last message of chatId
        val lastMessageResponse = MutableLiveData<Response<Message?>>()
        val chatRef = database.reference.child("chats").child(chatId)
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("lastMessage").exists()) {
                    val id = snapshot.child("lastMessage/id").value as String
                    val senderId = snapshot.child("lastMessage/senderId").value as String
                    val content = snapshot.child("lastMessage/content").value as String
                    val type =
                        MessageType.fromString(snapshot.child("lastMessage/type").value as String)
                    val status = if (auth.uid == senderId) {
                        MessageStatus.SENT
                    } else {
                        MessageStatus.RECEIVED
                    }
                    val time = snapshot.child("lastMessage/time").value as Long
                    val message = Message(id, senderId, chatId, content, status, type, time)
                    lastMessageResponse.postValue(Response.Success(message))
                } else {
                    lastMessageResponse.postValue(Response.Success(null))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                lastMessageResponse.postValue(Response.Failure(error.toException()))
            }
        })
        return lastMessageResponse
    }

    override suspend fun deleteChat(friend: FriendModel): Response<Boolean> {
        val chatId = if (auth.uid!! > friend.id!!) {
            "${auth.uid}-${friend.id}"
        } else {
            "${friend.id}-${auth.uid}"
        }
        return try {
            val chatRef = database.reference.child("messages").child(chatId)
            chatRef.removeValue().await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun readMessage(
        friend: FriendModel,
        lastMessage: Message?
    ): MutableLiveData<Response<List<Message>>> {
        val chatId = if (auth.uid!! > friend.id!!) {
            "${auth.uid}-${friend.id}"
        } else {
            "${friend.id}-${auth.uid}"
        }
        val messagesResponse = MutableLiveData<Response<List<Message>>>()
        val messageRef = database.reference.child("messages").child(chatId)
        if (lastMessage == null) {
            messageRef.limitToLast(100).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = mutableListOf<Message>()
                    snapshot.children.forEach {
                        try {
                            val id = it.child("id").value as String
                            val senderId = it.child("senderId").value as String
                            val content = it.child("content").value as String
                            val type = MessageType.fromString(it.child("type").value as String)
                            val status = if (auth.uid == senderId) {
                                MessageStatus.SENT
                            } else {
                                MessageStatus.RECEIVED
                            }
                            val time = it.child("time").value as Long
                            val message = Message(id, senderId, chatId, content, status, type, time)
                            messages.add(message)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    messagesResponse.postValue(Response.Success(messages))
                }

                override fun onCancelled(error: DatabaseError) {
                    messagesResponse.postValue(Response.Failure(error.toException()))
                }
            })
        } else {
            messageRef.orderByChild("time").endAt(lastMessage.messageTime.toDouble())
                .limitToLast(50)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val messages = mutableListOf<Message>()
                        snapshot.children.forEach {
                            try {
                                val id = it.child("id").value as String
                                val senderId = it.child("senderId").value as String
                                val content = it.child("content").value as String
                                val type = MessageType.fromString(it.child("type").value as String)
                                val status = if (auth.uid == senderId) {
                                    MessageStatus.SENT
                                } else {
                                    MessageStatus.RECEIVED
                                }
                                val time = it.child("time").value as Long
                                val message =
                                    Message(id, senderId, chatId, content, status, type, time)
                                if (message.messageTime < lastMessage.messageTime) {
                                    messages.add(message)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        messagesResponse.postValue(Response.Success(messages))
                    }

                    override fun onCancelled(error: DatabaseError) {
                        messagesResponse.postValue(Response.Failure(error.toException()))
                    }
                })
        }
        return messagesResponse
    }

    override fun getAllChat(): MutableLiveData<Response<List<Chat>>> {
        val roomResponse = MutableLiveData<Response<List<Chat>>>()
        roomResponse.postValue(Response.Loading)
        val roomReference = database.reference.child("chats")
        val roomListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                coroutineScope.launch {
                    roomResponse.postValue(Response.Success(getListRoom(snapshot)))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                roomResponse.postValue(Response.Failure(error.toException()))
            }
        }
        roomReference.addValueEventListener(roomListener)
        return roomResponse
    }

    private suspend fun getFriendProfile(friendId: String): FriendModel {
        return try {
            val friendProfile =
                database.reference.child("users").child(friendId).child("profile").get().await()
            FriendModel(
                friendId,
                friendProfile.child("display_name").value.toString(),
                friendProfile.child("profile_picture").value.toString(),
                FriendState.FRIEND
            )
        } catch (e: Exception) {
            throw e
        }
    }


    private suspend fun getLastMessage(roomId: String): Message {
        return try {
            val lastMessage =
                database.reference.child("chats").child(roomId).child("lastMessage").get().await()
            val id = lastMessage.child("id").value.toString()
            val senderId = lastMessage.child("senderId").value.toString()
            val content = lastMessage.child("content").value.toString()
            val type = MessageType.fromString(lastMessage.child("type").value.toString())
            val status = if (auth.uid == senderId) {
                MessageStatus.SENT
            } else {
                MessageStatus.RECEIVED
            }
            val time = lastMessage.child("time").value as Long
            Message(id, senderId, roomId, content, status, type, time)
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getListRoom(snapshot: DataSnapshot): List<Chat> {
        val listRoom = mutableListOf<Chat>()
        for (room in snapshot.children) {
            val id = room.key.toString()
            if (auth.uid != null && id.contains(auth.uid!!)) {
                val friendId = id.replace(auth.uid!!, "").replace("-", "")
                val friendProfile = getFriendProfile(friendId)
                val lastMessage = getLastMessage(id)
                Log.d(
                    "trndwcs",
                    lastMessage.content.toString() + " " + friendProfile.name.toString() + " " + id
                )
                listRoom.add(
                    Chat(
                        room.child("id").value.toString(),
                        friendId,
                        friendProfile.name,
                        friendProfile.profileImage,
                        lastMessage
                    )
                )
            }
        }
        return listRoom.sortedBy {
            -it.lastMessage!!.messageTime
        }.map {
            it
        }.toList()
    }

}