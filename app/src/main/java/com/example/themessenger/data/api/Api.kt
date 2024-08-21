package com.example.themessenger.data.api

import com.example.themessenger.data.api.models.IsSuccess
import com.example.themessenger.data.api.models.PhoneBase
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("send-auth-code/")
    suspend fun postPhone(@Body phoneBase: PhoneBase) : IsSuccess
}