package com.example.themessenger.presentation

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.themessenger.data.api.Api
import com.example.themessenger.data.api.models.CheckAuthCode
import com.example.themessenger.data.api.models.PhoneBase
import com.example.themessenger.data.api.models.RefreshToken
import com.example.themessenger.data.api.models.RegisterIn
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

    private val timer = Timer()

    init {
        timer.schedule(object : TimerTask() {
            override fun run() {
                refreshToken()
            }
        }, 0, 9 * 60 * 1000)
    }


    fun setMobileNumber(mobileNumber: String) {
        this.mobileNumber = mobileNumber
    }

    fun getMobileNumber(): String {
        return mobileNumber
    }

    fun setUserName(username: String) {
        this.username = username
    }

    fun getUserName(): String {
        return username
    }

    fun setAuthCode(authCode: String) {
        this.authCode = authCode
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
                    // Храним refreshToken в shared preferences
                    val refreshToken = responseBody?.refresh_token
                    sharedPreferences.edit().putString("refresh_token", refreshToken).apply()
                    if (refreshToken != null) {
                        _refreshToken = refreshToken
                    }
                    Log.d("Check", "Успешный ответ: ${response.body()}")
                    withContext(Dispatchers.Main) {
                        navController.navigate(NavRoute.Chats.route)
                    }
                } else {
                    Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Check", "Ошибка: $e")
            }
        }
    }

    fun refreshToken() {
        scope.launch {
            try {
                val response = apiWithToken.refreshToken(
                    RefreshToken(refresh_token = _refreshToken),
                    authorization = "Bearer $_accessToken"
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Check", "Успешный ответ: ${response.body()}")
                    if (responseBody != null) {
                        val newRefreshToken = responseBody.refresh_token
                        val newAccessToken = responseBody.access_token
                        sharedPreferences.edit().putString("refresh_token", newRefreshToken).apply()
                        sharedPreferences.edit().putString("access_token", newAccessToken).apply()
                        if (newRefreshToken != null) {
                            _refreshToken = newRefreshToken
                        }
                        if (newAccessToken != null) {
                            _accessToken = newAccessToken
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

    fun getCurrentUser() {
        scope.launch {
            try {
                val response = apiWithToken.getCurrentUser("Bearer $_accessToken")
                if (response.isSuccessful) {
                    val user = response.body()

                } else {
                    Log.e("Error", "Ошибка: ${response.code()} ${response.message()}")
                }
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
    private val clientWithToken = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptorWithToken)
        .addInterceptor(AuthorizationInterceptor(_refreshToken))
        .build()
    private val retrofitWithToken =
        Retrofit.Builder()
            .baseUrl("https://plannerok.ru/api/v1/users/")
            .client(clientWithToken)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val apiWithToken = retrofitWithToken.create(Api::class.java)

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

}
private class AuthorizationInterceptor(private val refreshToken: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", "Bearer $refreshToken")
            .build()
        return chain.proceed(authenticatedRequest)
    }
}
