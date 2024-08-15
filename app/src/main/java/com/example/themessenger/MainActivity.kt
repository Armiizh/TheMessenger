package com.example.themessenger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.themessenger.screens.login.PhoneNumberScreen
import com.example.themessenger.ui.theme.TheMessengerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheMessengerTheme {
                PhoneNumberScreen()
            }
        }
    }
}