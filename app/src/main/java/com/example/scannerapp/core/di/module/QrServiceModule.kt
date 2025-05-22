package com.example.scannerapp.core.di.module

import android.content.Context
import com.example.scannerapp.service.QrGeneratorServices
import com.example.scannerapp.service.QrGeneratorServiceImpl
import com.example.scannerapp.service.QrScannerService
import com.example.scannerapp.service.QrScannerServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class QrServiceModule {

    @Provides
    @Singleton
    fun provideQrGeneratorService(
        @ApplicationContext context: Context
    ): QrGeneratorServices {
        return QrGeneratorServiceImpl(context)
    }

    @Provides
    @Singleton
    fun provideQrScannerService():QrScannerService{
        return QrScannerServiceImpl()
    }

}