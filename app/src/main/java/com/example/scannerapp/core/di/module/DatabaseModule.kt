package com.example.scannerapp.core.di.module

import android.content.Context
import androidx.room.Room
import com.example.scannerapp.core.db.AppDatabase
import com.example.scannerapp.core.db.QrCodeDeo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
       @ApplicationContext  context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "scannerappv2",

        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): QrCodeDeo {
        return database.qrCodeInfo()
    }
}
