package com.example.themessenger.presentation

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.themessenger.data.room.AppDatabase
import com.example.themessenger.domain.MainViewModel
import com.example.themessenger.presentation.navigation.NavHostMessenger
import com.example.themessenger.presentation.ui.theme.TheMessengerTheme
import android.Manifest.permission.POST_NOTIFICATIONS

class MainActivity : ComponentActivity() {

    companion object { lateinit var database: AppDatabase }

    private var REQUEST_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermission()

        setContent {
            TheMessengerTheme {
                database = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "my_database"
                ).build()
                val viewModel = MainViewModel(application)
                val navController = rememberNavController()

                NavHostMessenger(viewModel, navController)

            }
        }
    }

    override fun onDestroy() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        super.onDestroy()
    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(POST_NOTIFICATIONS),
                    REQUEST_PERMISSION_CODE)
            }
        }
    }
}
