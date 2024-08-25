package com.example.themessenger.data.api

import com.example.themessenger.data.api.models.CheckAuthCode
import com.example.themessenger.data.api.models.IsSuccess
import com.example.themessenger.data.api.models.LoginOut
import com.example.themessenger.data.api.models.PhoneBase
import com.example.themessenger.data.api.models.RefreshToken
import com.example.themessenger.data.api.models.RegisterIn
import com.example.themessenger.data.api.models.Token
import com.example.themessenger.data.api.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Api {

    @POST("send-auth-code/")
    suspend fun sendPhoneNumber(@Body phoneBase: PhoneBase): Response<IsSuccess>

    @POST("check-auth-code/")
    suspend fun checkAuthCode(@Body checkAuthCode: CheckAuthCode): Response<LoginOut>

    @POST("register/")
    suspend fun register(@Body reg: RegisterIn): Response<Token>

    @GET("me/")
    suspend fun getCurrentUser(): Response<User>

    @POST("refresh-token/")
    suspend fun refreshToken(
        @Body refreshToken: RefreshToken
    ): Response<Token>
}