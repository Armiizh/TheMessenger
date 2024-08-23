package com.example.themessenger

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import com.example.themessenger.presentation.navigation.NavHostMessenger
import com.example.themessenger.presentation.ui.theme.TheMessengerTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.themessenger.data.room.AppDatabase
import com.example.themessenger.presentation.MainViewModel

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheMessengerTheme {
                val context = LocalContext.current
                val mViewModel: MainViewModel =
                    viewModel(factory = MainViewModel.MainViewModelFactory(context.applicationContext as Application))
                val navController = rememberNavController()
                database = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "my_database"
                ).build()
                NavHostMessenger(mViewModel, navController)
            }
        }
    }
}