package com.example.themessenger.screens.chats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.themessenger.MainViewModel
import com.example.themessenger.navigation.NavRoute

@Composable
fun ChatScreen(navController: NavHostController, viewModel: MainViewModel) {
    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Chat Screen")
            }
            Row {
                Button(onClick = { navController.navigate(NavRoute.Chats.route) }) {
                    Text(text = "Back to Chats")
                }
            }
        }
    )
}