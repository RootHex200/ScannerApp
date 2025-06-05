package com.example.scannerapp.core.di.module

import com.example.scannerapp.data.repositories.QrCodeRepositoryImpl
import com.example.scannerapp.domain.repositories.QrCodeRepositories
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        impl: QrCodeRepositoryImpl
    ):QrCodeRepositories

}