package com.example.themessenger.screens.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.themessenger.MainViewModel
import com.example.themessenger.navigation.NavRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(navController: NavHostController, viewModel: MainViewModel) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 2.dp,
                                end = 12.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.clickable {

                            }
                        )
                        Text(text = "Chats", style = MaterialTheme.typography.headlineLarge)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                Text(text = "Chats Screen")
                Row(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = { navController.navigate(NavRoute.Profile.route) }) {
                        Text(text = "Profile")
                    }
                    Button(onClick = { navController.navigate(NavRoute.Chat.route) }) {
                        Text(text = "Chat")
                    }
                    Button(onClick = { navController.navigate(NavRoute.Login.route) }) {
                        Text(text = "Exit")
                    }
                }
            }

        }
    )
}