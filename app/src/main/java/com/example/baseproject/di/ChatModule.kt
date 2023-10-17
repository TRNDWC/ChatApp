package com.example.baseproject.di


import com.example.baseproject.repository.ChatRepository
import com.example.baseproject.repositoryImpl.ChatRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ChatModule {
    @Provides
    fun provideChatRepository(): ChatRepository = ChatRepositoryImpl()
}