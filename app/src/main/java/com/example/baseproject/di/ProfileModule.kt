package com.example.baseproject.di

import com.example.baseproject.repository.ProfileRepository
import com.example.baseproject.repositoryImpl.ProfileRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ProfileModule {
    @Provides
    fun provideProfileRepository(): ProfileRepository = ProfileRepositoryImpl()
}
