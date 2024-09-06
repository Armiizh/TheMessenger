package com.example.themessenger.domain

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.themessenger.data.api.Api
import com.example.themessenger.data.api.models.Avatar
import com.example.themessenger.data.api.models.CheckAuthCode
import com.example.themessenger.data.api.models.PhoneBase
import com.example.themessenger.data.api.models.PutUser
import com.example.themessenger.data.api.models.RefreshToken
import com.example.themessenger.data.api.models.RegisterIn
import com.example.themessenger.data.room.AppDatabase
import com.example.themessenger.data.room.dao.UserDao
import com.example.themessenger.data.room.model.Avatar64
import com.example.themessenger.data.room.model.Avatars
import com.example.themessenger.data.room.model.UserEntity
import com.example.themessenger.presentation.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Base64
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appDatabase: AppDatabase,
    private val userDao: UserDao,
    application: Application
) : ViewModel() {

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

    fun getUserId(): Int {
        return sharedPreferences.getString("user_id", "")!!.toInt()
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
                        phone = mobileNumber, code = authCode
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
                        phone = mobileNumber, name = name, username = username
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
                                avatar = "", bigAvatar = "", miniAvatar = ""
                            )
                        }
                        val avatarBase64 = user.avatar?.toBase64()
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
                            },
                            avatar64 = Avatar64(base_64 = avatarBase64.toString(), filename = user.avatar.toString())
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

    fun putUser() {
        viewModelScope.launch {
            val user = getUser()
            val avatarBase64 = user?.avatar?.toBase64()
            try {
                val response = apiWithToken.putUser(
                    PutUser(
                        name = user?.name.toString(),
                        username = user?.username.toString(),
                        birthday = user?.birthday.toString(),
                        city = user?.city.toString(),
                        vk = user?.vk.toString(),
                        instagram = user?.instagram.toString(),
                        status = user?.status.toString(),
                        avatar = Avatar(base_64 = avatarBase64.toString(), filename = user?.avatar.toString())
                    )
                )
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val avatar = Avatars(
                            avatar = responseBody.avatar,
                            bigAvatar = responseBody.bigAvatar,
                            miniAvatar = responseBody.miniAvatar
                        )
                        val userr = UserEntity(
                            avatars = avatar,
                            avatar64 = Avatar64(base_64 = avatarBase64.toString(), filename = user?.avatar.toString())
                        )
                        updateUser(userr)
                    } else {
                        Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                    }
                } else {
                    Log.e("Check", "Ошибка: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Error", "Ошибка: $e")
            }
        }
    }
    fun String.toBase64(): String {
        return Base64.getEncoder().encodeToString(this.toByteArray())
    }

    suspend fun getUser(): UserEntity? = viewModelScope.async(Dispatchers.IO) {
        appDatabase.userDao().getUser(id = getUserId())
    }.await()

    suspend fun updateUser(userEntity: UserEntity) = viewModelScope.async(Dispatchers.IO) {
        userDao.updateUser(userEntity)
    }.await()

    val chuckerCollector = ChuckerCollector(
        context = application,
        showNotification = true,
        retentionPeriod = RetentionManager.Period.ONE_HOUR
    )
    val chuckerInterceptor = ChuckerInterceptor.Builder(application).collector(chuckerCollector)
        .maxContentLength(250_000L).redactHeaders("Auth-Token", "Bearer")
        .alwaysReadResponseBody(true).createShortcut(true).build()


    //Api Without Header
    private val interceptorWithoutToken = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val clientWithoutToken =
        OkHttpClient.Builder().addNetworkInterceptor(interceptorWithoutToken)
            .addInterceptor(chuckerInterceptor).build()
    private val retrofitWithoutToken =
        Retrofit.Builder().baseUrl("https://plannerok.ru/api/v1/users/").client(clientWithoutToken)
            .addConverterFactory(GsonConverterFactory.create()).build()
    private val apiWithoutToken = retrofitWithoutToken.create(Api::class.java)

    //Api With Header
    private val interceptorWithToken = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private var clientWithToken = OkHttpClient.Builder().addNetworkInterceptor(interceptorWithToken)
        .addInterceptor(AuthorizationInterceptor(this)).addInterceptor(chuckerInterceptor).build()
    private var retrofitWithToken =
        Retrofit.Builder().baseUrl("https://plannerok.ru/api/v1/users/").client(clientWithToken)
            .addConverterFactory(GsonConverterFactory.create()).build()
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

    fun determineZodiacSign(birthDate: String): String {
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
        val authenticatedRequest =
            request.newBuilder().header("Authorization", "Bearer $accessToken").build()

        Log.d("CheckToken", "CheckInAuthInterceptor $accessToken")
        return chain.proceed(authenticatedRequest)
    }
}