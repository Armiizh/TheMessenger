package com.example.themessenger.di

import android.content.Context
import androidx.room.Room
import com.example.themessenger.data.room.AppDatabase
import com.example.themessenger.data.room.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, "users")
            .build()
    }

    @Provides
    fun provideDaoPos(database: AppDatabase): UserDao {
        return database.userDao()
    }
}