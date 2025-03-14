package com.example.scannerapp.db

import android.content.Context
import androidx.room.Database

import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QRHistoryInfo::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun qrHistoryDao():QRHistoryDao

    companion object{
        private var appdb:AppDatabase?=null;
        fun getInstance(context: Context):AppDatabase{
            appdb=Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"scannerapp").allowMainThreadQueries().build()
            return appdb!!;
        }
    }
}

