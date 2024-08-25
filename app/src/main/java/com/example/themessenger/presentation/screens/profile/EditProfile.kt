package com.example.themessenger.presentation.screens.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavHostController
import com.example.themessenger.R
import com.example.themessenger.data.room.dao.UserDao
import com.example.themessenger.data.room.model.UserEntity
import com.example.themessenger.presentation.MainActivity
import com.example.themessenger.presentation.navigation.NavRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun EditProfile(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val userIdString = sharedPreferences.getString("user_id", "")
    var userEntity by remember { mutableStateOf<UserEntity?>(null) }
    val userDao = MainActivity.database.userDao()
    if (userIdString != null) {
        LaunchedEffect(Unit) {
            val userId = userIdString.toInt()
            userEntity = userDao.getUser(userId)
        }
    }

    Scaffold(
        containerColor = colorResource(id = R.color.LightLightGray),
        topBar = {
            TopAppBar(navController)
        },
        content = { paddingValues ->
            Content(paddingValues, userEntity, navController, userDao)
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
                Spacer(modifier = Modifier.weight(0.7f))
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
private fun Content(
    paddingValues: PaddingValues,
    userEntity: UserEntity?,
    navController: NavHostController,
    userDao: UserDao
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { saveImageToInternalStorage(context, it, userEntity, userDao, scope) }
        }
    )
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
                .clip(CircleShape)
                .clickable { launcher.launch("image/*") },
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
                containerColor = Color.White
            )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Телефон",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    text = "+${userEntity?.phone}",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                    color = Color.Black
                                )
                            }
                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Никнейм",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    text = "${userEntity?.username}",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                    color = Color.Black
                                )
                            }
                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Имя",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    text = "${userEntity?.name}",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                    color = Color.Black
                                )
                            }
                            IconButton(
                                onClick = {
                                    navController.navigate(NavRoute.EditName.route)
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.right_arrow),
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Город",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    text = "${userEntity?.city}",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                    color = Color.Black
                                )
                            }
                            IconButton(
                                onClick = {
                                    navController.navigate(NavRoute.EditCity.route)
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.right_arrow),
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Дата рождения",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    text = "${userEntity?.birthday}",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                    color = Color.Black
                                )
                            }
                            IconButton(
                                onClick = {
                                    navController.navigate(NavRoute.EditBirthday.route)
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.right_arrow),
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Знак зодиака",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    text = "${userEntity?.zodiacSign}",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                    color = Color.Black
                                )
                            }
                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "О себе",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    text = "${userEntity?.status}",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                    color = Color.Black
                                )
                            }
                            IconButton(
                                onClick = {
                                    navController.navigate(NavRoute.EditStatus.route)
                                }
                            ) {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.right_arrow),
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri, userEntity: UserEntity?, userDao: UserDao, scope: CoroutineScope) {
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput("image.jpg", Context.MODE_PRIVATE)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    scope.launch {
        userDao.updateUser(
            id = userEntity?.id,
            name = userEntity?.name,
            phone = userEntity?.phone,
            username = userEntity?.username,
            city = userEntity?.city,
            birthday = userEntity?.birthday,
            zodiacSign = userEntity?.zodiacSign,
            status = userEntity?.status,
            avatar = uri.toString()
        )
    }
}