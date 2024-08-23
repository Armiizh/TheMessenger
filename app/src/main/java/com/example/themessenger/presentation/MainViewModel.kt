package com.example.themessenger.presentation

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.themessenger.MainActivity
import com.example.themessenger.data.api.Api
import com.example.themessenger.data.api.models.CheckAuthCode
import com.example.themessenger.data.api.models.PhoneBase
import com.example.themessenger.data.api.models.RefreshToken
import com.example.themessenger.data.api.models.RegisterIn
import com.example.themessenger.data.room.model.UserEntity
import com.example.themessenger.presentation.navigation.NavRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
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

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var mobileNumber: String = ""
    private var authCode: String = ""
    var name: String = ""
    private var username: String = ""


    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private var _refreshToken: String = ""
    private var _accessToken: String = ""


    init {
        val refreshToken = sharedPreferences.getString("refresh_token", "")
        val accessToken = sharedPreferences.getString("accessToken", "")
        if (refreshToken != null && accessToken != null) {
            _refreshToken = refreshToken
            _accessToken = accessToken
        }
    }
    private fun getAccessToken(): String {
        return sharedPreferences.getString("access_token", "") ?: ""
    }

    private fun getRefreshToken(): String {
        return sharedPreferences.getString("refresh_token", "") ?: ""
    }

    private val timer = Timer()

    init {
        timer.schedule(object : TimerTask() {
            override fun run() {
                refreshToken()
            }
        }, 0, 2 * 60 * 1000)
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun postPhoneNumber(navController: NavHostController) {
        scope.launch {
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
                Log.d(
                    "Check", "postPhone\n" +
                            "mobileNumber - $mobileNumber," +
                            "\ncode - $authCode," +
                            "\n accessToken - $_accessToken," +
                            "\n refreshToken - $_refreshToken"
                )
            } catch (e: Exception) {
                Log.e("Check", "Ошибка: $e")
            }
        }

    }

    fun postAuthCode(navController: NavHostController) {
        scope.launch {
            try {
                val response = apiWithoutToken.checkAuthCode(
                    CheckAuthCode(
                        phone = mobileNumber,
                        code = authCode
                    )
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Check", "Успешный ответ: ${response.body()}")
                    if (responseBody != null) {
                        val refreshToken = responseBody.refresh_token
                        sharedPreferences.edit().putString("refresh_token", refreshToken).apply()
                        if (refreshToken != null) {
                            _refreshToken = refreshToken
                        }
                        if (!responseBody.is_user_exists!!) {
                            withContext(Dispatchers.Main) {
                                navController.navigate(NavRoute.Register.route)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                navController.navigate(NavRoute.Chats.route)
                            }
                        }
                    } else {
                        Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                    }
                } else {
                    Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                }
                Log.d(
                    "Check", "postCode\n" +
                            "mobileNumber - $mobileNumber," +
                            "\ncode - $authCode," +
                            "\n accessToken - $_accessToken," +
                            "\n refreshToken - $_refreshToken"
                )
            } catch (e: Exception) {
                Log.e("Check", "Ошибка: $e")
            }
        }
    }

    fun register(navController: NavHostController) {
        scope.launch {
            try {
                val response = apiWithoutToken.register(
                    RegisterIn(
                        phone = mobileNumber,
                        name = name,
                        username = username
                    )
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val refreshToken = responseBody?.refresh_token
                    val accessToken = responseBody?.access_token
                    sharedPreferences.edit().putString("refresh_token", refreshToken).apply()
                    if (refreshToken != null) {
                        _refreshToken = refreshToken
                    }
                    sharedPreferences.edit().putString("accessToken", accessToken).apply()
                    if (accessToken != null) {
                        _accessToken = accessToken
                    }

                    Log.d("Check", "Успешный ответ: ${response.body()}")
                    withContext(Dispatchers.Main) {
                        navController.navigate(NavRoute.Chats.route)
                    }
                } else {
                    Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                }
                Log.d(
                    "Check", "Register\n" +
                            "mobileNumber - $mobileNumber," +
                            "\ncode - $authCode," +
                            "\n accessToken - $_accessToken," +
                            "\n refreshToken - $_refreshToken"
                )
            } catch (e: Exception) {
                Log.e("Check", "Ошибка: $e")
            }
        }
    }

    fun refreshToken() {
        scope.launch {
            try {
                val response = apiWithToken.refreshToken(
                    RefreshToken(refresh_token = getRefreshToken()),
                    authorization = "Bearer ${getAccessToken()}"
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Check", "Успешный ответ: ${response.body()}")
                    if (responseBody != null) {
                        val newRefreshToken = responseBody.refresh_token
                        val newAccessToken = responseBody.access_token
                        sharedPreferences.edit().putString("refresh_token", newRefreshToken).apply()
                        sharedPreferences.edit().putString("access_token", newAccessToken).apply()
                        _refreshToken = newRefreshToken!!
                        _accessToken = newAccessToken!!


                    } else {
                        Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                    }
                } else {
                    Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                }
                Log.d(
                    "Check", "Refresh\n" +
                            "mobileNumber - $mobileNumber," +
                            "\naccessToken - ${getRefreshToken()}," +
                            "\nrefreshToken - ${getAccessToken()}"
                )
            } catch (e: Exception) {
                Log.e("Check", "Ошибка: $e")
            }
        }
    }

    fun getCurrentUser() {
        scope.launch {
            try {
                val response = apiWithToken
                    .getCurrentUser("Bearer ${getAccessToken()}")
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val userEntity = UserEntity(
                            id = responseBody.profileData.id,
                            avatar = responseBody.profileData.avatar,
                            phone = responseBody.profileData.phone,
                            nickname = responseBody.profileData.username,
                            city = responseBody.profileData.city,
                            birthday = responseBody.profileData.birthday,
                            zodiac = "",
                            about = ""
                        )
                        val userDao = MainActivity.database.userDao()
                        userDao.insertUser(userEntity)
                    }
                } else {
                    Log.e("Error", "Ошибка: ${response.code()} ${response.message()}")
                }
                Log.d(
                    "CheckToken", "GetCurrentUser\n" +
                            "mobileNumber - $mobileNumber," +
                            "\naccessToken - ${getRefreshToken()}," +
                            "\nrefreshToken - ${getAccessToken()}"
                )
            } catch (e: Exception) {
                Log.e("Error", "Ошибка: $e")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class MainViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(application = application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
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
        .addInterceptor(AuthorizationInterceptor(getAccessToken()))
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

    fun setUserName(username: String) {
        this.username = username
    }

    fun setAuthCode(authCode: String) {
        this.authCode = authCode
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}

private class AuthorizationInterceptor(private val accessToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        Log.d(
            "CheckToken", "CheckInAuthInterceptor $accessToken"
        )
        return chain.proceed(authenticatedRequest)
    }
}
