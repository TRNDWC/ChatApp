package com.example.baseproject.di

import com.example.baseproject.repository.FriendShipsRepository
import com.example.baseproject.repositoryImpl.FriendShipsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class FriendShipModule {
    @Provides
    fun provideFriendShipRepository(): FriendShipsRepository = FriendShipsRepositoryImpl()
}