package com.example.themessenger.data.room

import androidx.room.TypeConverter
import com.example.themessenger.data.room.model.Avatar64
import com.google.gson.Gson

class Avatar64Converter {
    @TypeConverter
    fun fromAvatar64(avatar64: Avatar64?): String? {
        return if (avatar64 != null) Gson().toJson(avatar64) else null
    }

    @TypeConverter
    fun toAvatar64(json: String?): Avatar64? {
        return if (json != null) Gson().fromJson(json, Avatar64::class.java) else null
    }
}