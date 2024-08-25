package com.example.themessenger.data.room

import androidx.room.TypeConverter
import com.example.themessenger.data.room.model.Avatars
import com.google.gson.Gson

class AvatarsConverter {
    @TypeConverter
    fun fromAvatars(avatars: Avatars?): String? {
        return if (avatars != null) Gson().toJson(avatars) else null
    }

    @TypeConverter
    fun toAvatars(json: String?): Avatars? {
        return if (json != null) Gson().fromJson(json, Avatars::class.java) else null
    }
}