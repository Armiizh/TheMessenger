package com.example.themessenger.presentation.screens.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.themessenger.R
import com.example.themessenger.data.room.model.UserEntity
import com.example.themessenger.presentation.MainActivity
import com.example.themessenger.presentation.navigation.NavRoute

@Composable
fun EditProfile(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val userIdString = sharedPreferences.getString("user_id", "")
    var userEntity by remember { mutableStateOf<UserEntity?>(null) }
    if (userIdString != null) {
        LaunchedEffect(Unit) {
            val userId = userIdString.toInt()
            val userDao = MainActivity.database.userDao()
            userEntity = userDao.getUser(userId)
        }
    }

    Scaffold(
        containerColor = colorResource(id = R.color.LightLightGray),
        topBar = {
            TopAppBar(navController)
        },
        content = { paddingValues ->
            Content(paddingValues, userEntity)
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(0.dp),
                content = {},
                containerColor = colorResource(id = R.color.LightLightGray)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(navController: NavHostController) {

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
                        .clickable {
                            navController.navigate(NavRoute.Profile.route)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.left_arrow),
                        contentDescription = "",
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Изменить",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                )
                Spacer(modifier = Modifier.weight(0.8f))
                Text(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { navController.navigate(NavRoute.Profile.route) },
                    text = "Готово",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_medium))
                )

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
private fun Content(paddingValues: PaddingValues, userEntity: UserEntity?) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(color = colorResource(id = R.color.LightLightGray)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            modifier = Modifier
                .size(180.dp)
                .padding(8.dp)
                .clip(CircleShape),
            painter = painterResource(id = R.drawable.person),
            contentDescription = ""
        )
        Text(
            text = "Фото профиля",
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.roboto_bold))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            elevation = CardDefaults.elevatedCardElevation(
                focusedElevation = 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.White
            )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    EditProfileItem("Имя", userEntity?.name ?: "Добавьте информацию")
                }
                item {
                    EditProfileItem("Никнейм", userEntity?.username ?: "Добавьте информацию")
                }
                item {
                    EditProfileItem("Город", userEntity?.city ?: "Добавьте информацию")
                }
                item {
                    EditProfileItem("Дата рождения", userEntity?.birthday ?: "Добавьте информацию")
                }
                item {
                    EditProfileItem("Знак зодиака", userEntity?.zodiacSign ?: "Добавьте информацию")
                }
                item {
                    EditProfileItem("О себе", userEntity?.status ?: "Добавьте информацию")
                }
            }
        }
    }
}

@Composable
fun EditProfileItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                color = Color.Black
            )
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = description,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                color = Color.Black
            )
            HorizontalDivider(
                modifier = modifier,
                thickness = 1.dp,
                color = Color.LightGray
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.right_arrow),
            contentDescription = "",
            tint = Color.Black
        )
    }
}