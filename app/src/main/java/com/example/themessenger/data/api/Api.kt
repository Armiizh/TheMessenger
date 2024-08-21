package com.example.themessenger.data.api

import com.example.themessenger.data.api.models.CheckAuthCode
import com.example.themessenger.data.api.models.IsSuccess
import com.example.themessenger.data.api.models.LoginOut
import com.example.themessenger.data.api.models.PhoneBase
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("send-auth-code/")
    suspend fun sendPhoneNumber(@Body phoneBase: PhoneBase) : Response<IsSuccess>

    @POST("check-auth-code/")
    suspend fun checkAuthCode(@Body checkAuthCode: CheckAuthCode) : Response<LoginOut>
}