package com.example.themessenger.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.themessenger.data.room.AppDatabase
import com.example.themessenger.domain.MainViewModel
import com.example.themessenger.presentation.navigation.NavHostMessenger

import com.example.themessenger.presentation.ui.theme.TheMessengerTheme

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
}