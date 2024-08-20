package com.example.themessenger.presentation.screens.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.themessenger.presentation.MainViewModel
import com.example.themessenger.R
import com.example.themessenger.presentation.navigation.NavRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class MenuItem(val iconId: Int, val text: String)

@Composable
fun ChatsScreen(navController: NavHostController, viewModel: MainViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val menuItems1 = listOf(
        MenuItem(R.drawable.favourites, "Избранное"),
        MenuItem(R.drawable.recentcalls, "Недавние звонки"),
        MenuItem(R.drawable.devices, "Устройства"),
        MenuItem(R.drawable.folders, "Папки с чатами"),
    )

    val menuItems2 = listOf(
        MenuItem(R.drawable.notification, "Уведомления и звуки"),
        MenuItem(R.drawable.confidentiality, "Конфиденциальность"),
        MenuItem(R.drawable.data, "Данные и память"),
        MenuItem(R.drawable.formalization, "Оформление"),
        MenuItem(R.drawable.battery, "Энергосбережение"),
        MenuItem(R.drawable.language, "Язык")
    )


    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        content = {
            Scaffold(
                topBar = {
                    ChatTopAppBar(scope, drawerState)
                },
                content = { paddingValues ->
                    ChatContent(paddingValues, navController)
                }
            )
        },
        drawerContent = {
            Scaffold(
                containerColor = colorResource(id = R.color.TopAppBarColor),
                snackbarHost = { SnackbarHost(snackbarHostState) },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .background(colorResource(id = R.color.TopAppBarColor)),
                topBar = {
                    DrawerTopAppBar()
                },
                content = { paddingValues ->
                    DrawerContent(
                        paddingValues,
                        navController,
                        menuItems1,
                        snackbarHostState,
                        menuItems2
                    )
                }
            )
        }
    )
}

@Composable
private fun DrawerContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    menuItems1: List<MenuItem>,
    snackbarHostState: SnackbarHostState,
    menuItems2: List<MenuItem>
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .background(color = colorResource(id = R.color.TopAppBarColor))
    ) {
        MyProfile(navController)
        DrawerItem(menuItem = menuItems1, snackbarHostState)
        DrawerItem(menuItem = menuItems2, snackbarHostState)
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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DrawerTopAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Меню",
                fontFamily = FontFamily(Font(R.font.roboto_medium))
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.TopAppBarColor),
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun ChatContent(
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = R.drawable.bg3),
        contentDescription = "",
        contentScale = ContentScale.Crop
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
    ) {
        items(10) { index ->
            ChatListItem(
                userName = "Пользователь $index",
                userIcon = Icons.Filled.Person,
                messageText = "Сообщение $index",
                messageDate = "12:34",
                unreadCount = if (index % 2 == 0) 0 else 1,
                navController = navController
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ChatTopAppBar(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Меню",
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                drawerState.open()
                            }
                        }
                )
                Text(
                    text = "Чаты",
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                )
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Filled.Add, contentDescription = ""
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.TopAppBarColor),
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun MyProfile(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp, bottom = 8.dp)
            .background(
                color = colorResource(id = R.color.DrawerContent),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { navController.navigate(NavRoute.Profile.route) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = "Профиль",
            tint = Color.Gray
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "Мой профиль",
            fontFamily = FontFamily(Font(R.font.roboto_light)),
            color = Color.LightGray,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp),
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Gray
        )
    }
}


@Composable
fun ChatListItem(
    userName: String,
    userIcon: ImageVector,
    messageText: String,
    messageDate: String,
    unreadCount: Int,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(NavRoute.Chat.route) },
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
                    contentDescription = ""
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


@Composable
private fun DrawerItem(menuItem: List<MenuItem>, snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp, bottom = 8.dp)
            .background(
                color = colorResource(id = R.color.DrawerContent),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        menuItem.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Нажата кнопка: ${item.text}",
                                duration = SnackbarDuration.Short
                            )
                        }

                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp),
                    painter = painterResource(id = item.iconId),
                    contentDescription = "",
                    tint = Color.Gray
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = item.text,
                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp),
                    painter = painterResource(id = R.drawable.right_arrow),
                    contentDescription = "",
                    tint = Color.Gray
                )
            }
        }
    }
}