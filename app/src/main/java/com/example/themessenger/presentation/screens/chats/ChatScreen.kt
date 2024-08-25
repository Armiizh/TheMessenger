package com.example.themessenger.presentation.screens.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.themessenger.R
import com.example.themessenger.domain.MainViewModel
import com.example.themessenger.presentation.navigation.NavRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavHostController, viewModel: MainViewModel) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var message by remember {
        mutableStateOf("")
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(navController, scope, snackbarHostState)
        },
        content = { paddingValues ->
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.bg1),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(top = 2.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val messages = listOf(
                    Message("Привет", true),
                    Message("Hello", false),
                    Message("Как дела?", true),
                    Message("Fine, thanks", false),
                    Message("А у тебя?", true),
                    Message("Good", false),
                    Message("Не желаешь ли встреться в субботу?", true),
                    Message("Sounds great, who's going to be there?", false),
                    Message("Все наши одноклассники, все таки встреча выпускников", true),
                    Message("I will", false),
                    Message("А почему ты пишешь на английском?", true),
                    Message("А почему бы и нет", false)

                )

                items(messages) { message ->
                    if (message.isMine) {
                        MineMessage(message.text, "18:34")
                    } else {
                        OtherMessage(message.text, "18:34")
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                content = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.paper_clip),
                            contentDescription = ""
                        )
                        TextField(
                            value = message,
                            onValueChange = {
                                message = it
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            placeholder = {
                                Text(
                                    text = "Введите сообщение...",
                                    color = Color.Black
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent
                            )
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.send),
                            contentDescription = ""
                        )
                    }
                },
                containerColor = colorResource(id = R.color.TopAppBarColor),
            )
        }

    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopAppBar(
    navController: NavHostController,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clickable { navController.navigate(NavRoute.Chats.route) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.left_arrow),
                        contentDescription = "",
                    )
                    Text(
                        text = "Чаты",
                        fontFamily = FontFamily(Font(R.font.roboto_medium))
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Переход на профиль собеседника",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = "Пользователь",
                        fontFamily = FontFamily(Font(R.font.roboto_medium))
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(id = R.color.TopAppBarColor),
            titleContentColor = Color.Black
        )
    )
}

@Composable
private fun MineMessage(text: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 2.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(align = Alignment.CenterEnd)
                .width(IntrinsicSize.Max)
                .background(
                    color = colorResource(id = R.color.Brown).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = text,
                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                    fontSize = 16.sp,
                    color = Color.White
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = time,
                        fontFamily = FontFamily(Font(R.font.roboto_light)),
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 4.dp),
                        imageVector = Icons.Filled.Done,
                        contentDescription = "",
                        tint = Color.White
                    )

                }
            }
        }
    }
}

@Composable
private fun OtherMessage(text: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(horizontal = 16.dp)
            .padding(top = 2.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(align = Alignment.CenterEnd)
                .width(IntrinsicSize.Max)
                .background(
                    color = Color.Gray.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = text,
                    fontFamily = FontFamily(Font(R.font.roboto_light)),
                    fontSize = 16.sp,
                    color = Color.White
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = time,
                        fontFamily = FontFamily(Font(R.font.roboto_light)),
                        fontSize = 12.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 4.dp),
                        imageVector = Icons.Filled.Done,
                        contentDescription = "",
                        tint = Color.White
                    )

                }
            }
        }
    }
}

data class Message(val text: String, val isMine: Boolean)