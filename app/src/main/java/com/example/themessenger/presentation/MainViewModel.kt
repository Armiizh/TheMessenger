package com.example.themessenger.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.themessenger.data.api.Api
import com.example.themessenger.data.api.models.CheckAuthCode
import com.example.themessenger.data.api.models.PhoneBase
import com.example.themessenger.presentation.navigation.NavRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var mobileNumber: String = ""
    private var authCode: String = ""

    fun setMobileNumber(mobileNumber: String) {
        this.mobileNumber = mobileNumber
    }
    fun setAuthCode(authCode: String) {
        this.authCode = authCode
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor)
        .build()
    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://plannerok.ru/api/v1/users/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private val api = retrofit.create(Api::class.java)

    fun postPhoneNumber(navController: NavHostController) {
        scope.launch {
            try {
                val response = api.sendPhoneNumber(
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
                val response = api.checkAuthCode(
                    CheckAuthCode(
                        phone = mobileNumber,
                        code = authCode
                    )
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("Check", "Успешный ответ: ${response.body()}")
                    if (responseBody != null) {
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


    @Suppress("UNCHECKED_CAST")
    class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(application = application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }


    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
