package com.example.themessenger.screens.chats

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.themessenger.MainViewModel
import com.example.themessenger.R
import com.example.themessenger.navigation.NavRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(navController: NavHostController, viewModel: MainViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        content = {
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
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                )
                                Text(
                                    text = "Чаты",
                                    fontFamily = FontFamily(Font(R.font.roboto_medium))
                                )
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
                        items(4) { index ->
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
        },
        drawerContent = {
            Scaffold(
                modifier = Modifier.fillMaxWidth(0.7f),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Меню",
                                fontFamily = FontFamily(Font(R.font.roboto_medium))
                            )
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
                        MyProfile(navController)
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(top = 8.dp, bottom = 8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Favourites()
                            Recentcalls()
                            Devices()
                            ChatFolders()
                        }

                        Column(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(top = 8.dp, bottom = 8.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            NotificationsAndSounds()
                            Confidentiality()
                            DataAndMemory()
                            Formalisation()
                            EnergySaving()
                            Language()
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = colorResource(id = R.color.Red),
                                contentColor = colorResource(id = R.color.white)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            onClick = { navController.navigate(NavRoute.Login.route) }) {
                            Text(
                                text = "Выйти",
                                fontFamily = FontFamily(Font(R.font.roboto_light))
                            )
                        }
                    }
                }
            )
        }
    )
}

@Composable
private fun ChatFolders() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = "sss"
        )
        Text(
            text = "Папки с чатами",
            fontFamily = FontFamily(Font(R.font.roboto_light))
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
}

@Composable
private fun Devices() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp).size(24.dp),
            painter = painterResource(id = R.drawable.devices),
            contentDescription = "sss"
        )
        Text(
            text = "Устройства",
            fontFamily = FontFamily(Font(R.font.roboto_light))
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
}

@Composable
private fun Recentcalls() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp).size(24.dp),
            painter = painterResource(id = R.drawable.recentcalls),
            contentDescription = "sss"
        )
        Text(
            text = "Недавние звонки",
            fontFamily = FontFamily(Font(R.font.roboto_light))
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
}

@Composable
private fun Favourites() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp).size(24.dp),
            painter = painterResource(id = R.drawable.favourites),
            contentDescription = "sss"
        )
        Text(
            text = "Избранное",
            fontFamily = FontFamily(Font(R.font.roboto_light))
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
}

@Composable
private fun Language() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = "sss"
        )
        Text(
            text = "Язык",
            fontFamily = FontFamily(Font(R.font.roboto_light))
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
}

@Composable
private fun EnergySaving() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = "sss"
        )
        Text(
            text = "Энергосбережение",
            fontFamily = FontFamily(Font(R.font.roboto_light))
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
}

@Composable
private fun Formalisation() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = "sss"
        )
        Text(
            text = "Оформление",
            fontFamily = FontFamily(Font(R.font.roboto_light))
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
}

@Composable
private fun DataAndMemory() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = "sss"
        )
        Text(
            text = "Данные и память",
            fontFamily = FontFamily(Font(R.font.roboto_light))
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
}

@Composable
private fun Confidentiality() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = "sss"
        )
        Text(
            text = "Конфиденциальность",
            fontFamily = FontFamily(Font(R.font.roboto_light))
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
}

@Composable
private fun NotificationsAndSounds() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = "Уведомления и звуки"
        )
        Text(
            text = "Уведомления и звуки",
            fontFamily = FontFamily(Font(R.font.roboto_light))
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
}

@Composable
private fun MyProfile(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp, bottom = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { navController.navigate(NavRoute.Profile.route) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = "Профиль"
        )
        Text(
            text = "Мой профиль",
            fontFamily = FontFamily(Font(R.font.roboto_light)),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray)
    }
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
                modifier = Modifier
                    .padding(4.dp)
                    .size(36.dp),
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
                            fontSize = 16.sp,
                            color = Color.White
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

