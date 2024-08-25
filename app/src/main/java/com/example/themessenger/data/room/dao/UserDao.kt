package com.example.themessenger.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.themessenger.data.room.model.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUser(id: Int?): UserEntity?

    @Query("UPDATE users SET name = :name, phone = :phone, username = :username, city = :city, birthday = :birthday, zodiacSign = :zodiacSign, status = :status, avatar = :avatar WHERE id = :id")
    suspend fun updateUser(
        id: Int?,
        name: String?,
        phone: String?,
        username: String?,
        city: String?,
        birthday: String?,
        zodiacSign: String?,
        status: String?,
        avatar: String?
    )
}