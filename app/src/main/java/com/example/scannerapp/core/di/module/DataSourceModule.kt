package com.example.scannerapp.core.di.module

import com.example.scannerapp.data.data_source.QrCodeDataSource
import com.example.scannerapp.data.data_source.QrCodeLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton

    abstract fun provideDataSource(
        impl: QrCodeLocalDataSource
    ):QrCodeDataSource
}