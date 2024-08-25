package com.example.themessenger.domain

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.themessenger.presentation.MainActivity
import com.example.themessenger.data.api.Api
import com.example.themessenger.data.api.models.CheckAuthCode
import com.example.themessenger.data.api.models.PhoneBase
import com.example.themessenger.data.api.models.RefreshToken
import com.example.themessenger.data.api.models.RegisterIn
import com.example.themessenger.data.room.model.Avatars
import com.example.themessenger.data.room.model.UserEntity
import com.example.themessenger.presentation.navigation.NavRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Timer
import java.util.TimerTask

class MainViewModel(private val application: Application): ViewModel() {

    private var mobileNumber: String = ""
    private var authCode: String = ""
    var name: String = ""
    var username: String = ""

    private var accessToken: String? = null
    private var refreshToken: String? = null

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun getAccessToken(): String {
        return sharedPreferences.getString("access_token", "") ?: ""
    }

    private fun getRefreshToken(): String {
        return sharedPreferences.getString("refresh_token", "") ?: ""
    }

    private val timer = Timer()

    init {
        accessToken = sharedPreferences.getString("access_token", "") ?: ""
        refreshToken = sharedPreferences.getString("refresh_token", "") ?: ""

        timer.schedule(object : TimerTask() {
            override fun run() {
                refreshToken()
            }
        }, 0, 9 * 60 * 1000)
    }


    fun postPhoneNumber(navController: NavHostController) {
        viewModelScope.launch {
            try {
                val response = apiWithoutToken.sendPhoneNumber(
                    PhoneBase(
                        phone = mobileNumber
                    )
                )
                if (response.isSuccessful) {
                    Log.d("Check", "Успешный ответ: ${response.body()}")
                    withContext(Dispatchers.Main) {
                        navController.navigate(NavRoute.Confirm.route)
                    }
                } else {
                    Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Check", "Ошибка: $e")
            }
        }
    }

    fun checkAuthCode(navController: NavHostController) {
        viewModelScope.launch {
            try {
                val response = apiWithoutToken.checkAuthCode(
                    CheckAuthCode(
                        phone = mobileNumber,
                        code = authCode
                    )
                )
                if (response.isSuccessful) {
                    Log.d("Check", "Успешный ответ: ${response.body()}")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val refreshToken = responseBody.refresh_token
                        val accessToken = responseBody.access_token
                        val userId = responseBody.user_id
                        sharedPreferences.edit().putString("refresh_token", refreshToken).apply()
                        sharedPreferences.edit().putString("access_token", accessToken).apply()
                        sharedPreferences.edit().putString("user_id", userId.toString()).apply()
                        if (!responseBody.is_user_exists!!) {
                            withContext(Dispatchers.Main) {
                                navController.navigate(NavRoute.Register.route)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                navController.navigate(NavRoute.Chats.route)
                                getCurrentUser()
                            }
                        }
                    } else {
                        Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                    }
                } else {
                    Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Check", "Ошибка: $e")
            }
        }
    }

    fun register(navController: NavHostController) {
        viewModelScope.launch {
            try {
                val response = apiWithoutToken.register(
                    RegisterIn(
                        phone = mobileNumber,
                        name = name,
                        username = username
                    )
                )
                if (response.isSuccessful) {
                    Log.d("Check", "responseBody is null")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val refreshToken = responseBody.refresh_token
                        val accessToken = responseBody.access_token
                        val userId = responseBody.user_id
                        sharedPreferences.edit().putString("refresh_token", refreshToken).apply()
                        sharedPreferences.edit().putString("access_token", accessToken).apply()
                        sharedPreferences.edit().putString("user_id", userId.toString()).apply()
                        Log.d("Check", "Успешный ответ: ${response.body()}")
                        withContext(Dispatchers.Main) {
                            navController.navigate(NavRoute.Chats.route)
                            getCurrentUser()
                        }
                    }
                } else {
                    Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Check", "Ошибка: $e")
            }
        }
    }

    private fun refreshToken() {
        viewModelScope.launch {
            try {
                val response = apiWithToken.refreshToken(
                    RefreshToken(refresh_token = refreshToken!!)
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Check", "Успешный ответ: ${response.body()}")
                    if (responseBody != null) {
                        val newRefreshToken = responseBody.refresh_token
                        val newAccessToken = responseBody.access_token
                        accessToken = newAccessToken
                        refreshToken = newRefreshToken
                        sharedPreferences.edit().putString("refresh_token", newRefreshToken).apply()
                        sharedPreferences.edit().putString("access_token", newAccessToken).apply()
                        getCurrentUser()
                    } else {
                        Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                    }
                } else {
                    Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Check", "Ошибка: $e")
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val response = apiWithToken.getCurrentUser()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val user = responseBody.profileData
                        val userDao = MainActivity.database.userDao()
                        val apiAvatars = user.avatars
                        var roomAvatars: Avatars? = null
                        if (apiAvatars != null) {
                            roomAvatars = Avatars(
                                avatar = apiAvatars.avatar,
                                bigAvatar = apiAvatars.bigAvatar,
                                miniAvatar = apiAvatars.miniAvatar
                            )
                        } else {
                            roomAvatars = Avatars(
                                avatar = "",
                                bigAvatar = "",
                                miniAvatar = ""
                            )
                        }
                        val userEntity = UserEntity(
                            name = user.name,
                            username = user.username,
                            birthday = user.birthday,
                            city = user.city,
                            vk = user.vk,
                            instagram = user.instagram,
                            status = user.status,
                            avatar = user.avatar,
                            id = user.id,
                            last = user.last,
                            online = user.online,
                            created = user.created,
                            phone = user.phone,
                            completedTask = user.completedTask,
                            avatars = roomAvatars,
                            zodiacSign = if (user.birthday != "") {
                                determineZodiacSign(user.birthday.toString())
                            } else {
                                ""
                            }
                        )
                        Log.d("CHECK", "${userEntity.phone}")
                        userDao.insertUser(userEntity)
                        Log.d("Database", "User data inserted successfully")

                    }
                } else {
                    Log.e("Error", "Ошибка: ${response.code()} ${response.message()}")
                }
                Log.d("CheckToken", "JWT: ${getAccessToken()}")
            } catch (e: Exception) {
                Log.e("Error", "Ошибка: $e")
            }
        }
    }

    //Api Without Header
    private val interceptorWithoutToken = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val clientWithoutToken = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptorWithoutToken)
        .build()
    private val retrofitWithoutToken =
        Retrofit.Builder()
            .baseUrl("https://plannerok.ru/api/v1/users/")
            .client(clientWithoutToken)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val apiWithoutToken = retrofitWithoutToken.create(Api::class.java)

    //Api With Header
    private val interceptorWithToken = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private var clientWithToken = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptorWithToken)
        .addInterceptor(AuthorizationInterceptor(this))
        .build()
    private var retrofitWithToken =
        Retrofit.Builder()
            .baseUrl("https://plannerok.ru/api/v1/users/")
            .client(clientWithToken)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private var apiWithToken = retrofitWithToken.create(Api::class.java)

    fun setMobileNumber(mobileNumber: String) {
        this.mobileNumber = mobileNumber
    }

    fun getMobileNumber(): String {
        return mobileNumber
    }

    fun setAuthCode(authCode: String) {
        this.authCode = authCode
    }


    fun logout() {
        sharedPreferences.edit().clear().apply()
    }

    private fun determineZodiacSign(birthDate: String): String {
        if (birthDate.isEmpty() || birthDate == null) {
            return "Неизвестный знак зодиака"
        }
        val parts = birthDate.split("-")
        if (parts.size != 3) {
            return "Неизвестный знак зодиака"
        }
        val year = parts[0]
        val month = parts[1]
        val day = parts[2]
        val monthInt = month.toInt()
        val dayInt = day.toInt()
        return when (monthInt) {
            1 -> if (dayInt >= 20) "Водолей" else "Козерог"
            2 -> if (dayInt >= 19) "Рыбы" else "Водолей"
            3 -> if (dayInt >= 21) "Овен" else "Рыбы"
            4 -> if (dayInt >= 20) "Телец" else "Овен"
            5 -> if (dayInt >= 21) "Близнецы" else "Телец"
            6 -> if (dayInt >= 21) "Рак" else "Близнецы"
            7 -> if (dayInt >= 23) "Лев" else "Рак"
            8 -> if (dayInt >= 23) "Дева" else "Лев"
            9 -> if (dayInt >= 23) "Весы" else "Дева"
            10 -> if (dayInt >= 23) "Скорпион" else "Весы"
            11 -> if (dayInt >= 22) "Стрелец" else "Скорпион"
            12 -> if (dayInt >= 22) "Козерог" else "Стрелец"
            else -> "Неизвестный знак зодиака"
        }
    }
}
private class AuthorizationInterceptor(private val viewModel: MainViewModel) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = viewModel.getAccessToken()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        Log.d("CheckToken", "CheckInAuthInterceptor $accessToken")
        return chain.proceed(authenticatedRequest)
    }
}