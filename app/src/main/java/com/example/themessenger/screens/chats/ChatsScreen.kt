package com.example.themessenger.screens.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.themessenger.MainViewModel
import com.example.themessenger.R
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
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Меню",
                            modifier = Modifier.clickable {

                            }
                        )
                        Text(text = "Чаты", style = MaterialTheme.typography.headlineLarge)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(10) { index ->
                    ChatListItem(
                        userName = "Пользователь $index",
                        userIcon = Icons.Filled.Person,
                        messageText = "Сообщение $index",
                        messageDate = "12:34",
                        unreadCount = if (index % 2 == 0) 0 else 1
                    )
                }
            }
        }
    )
}


@Composable
fun ChatListItem(
    userName: String,
    userIcon: ImageVector,
    messageText: String,
    messageDate: String,
    unreadCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(4.dp).size(36.dp),
                imageVector = userIcon,
                contentDescription = "User Icon"
            )
            Column {
                Text(text = userName, fontFamily = FontFamily(Font(R.font.roboto_medium)))
                Text(text = messageText, fontFamily = FontFamily(Font(R.font.roboto_light)))
            }
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(text = messageDate, fontFamily = FontFamily(Font(R.font.roboto_light)))
            if (unreadCount > 0) {
                BadgeBox(
                    badgeContent = {
                        Text(
                            text = unreadCount.toString(),
                            fontFamily = FontFamily(Font(R.font.roboto_light)),
                            fontSize = 16.sp
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun BadgeBox(
    badgeContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .offset(x = 0.dp, y = (-2).dp),
        contentAlignment = Alignment.Center
    ) {
        badgeContent()
    }
}
