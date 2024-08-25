package com.example.themessenger.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.themessenger.data.room.dao.UserDao
import com.example.themessenger.data.room.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
@TypeConverters(AvatarsConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}