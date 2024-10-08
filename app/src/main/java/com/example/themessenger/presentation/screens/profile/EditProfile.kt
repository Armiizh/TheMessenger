package com.example.themessenger.presentation.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.themessenger.R
import com.example.themessenger.data.room.model.UserEntity
import com.example.themessenger.domain.MainViewModel
import com.example.themessenger.presentation.navigation.NavRoute
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun EditProfile(navController: NavHostController, viewModel: MainViewModel) {

    val userEntityState = runBlocking { viewModel.getUser() }
    val userEntity by remember { mutableStateOf(userEntityState) }

    val imageUri = remember { mutableStateOf(userEntity?.avatar?.let { Uri.parse(it) }) }

    Scaffold(
        containerColor = colorResource(id = R.color.LightLightGray),
        topBar = {
            TopAppBar(navController, viewModel, imageUri)
        },
        content = { paddingValues ->
            Content(paddingValues, userEntity, navController, imageUri)
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
private fun TopAppBar(
    navController: NavHostController,
    viewModel: MainViewModel,
    imageUri: MutableState<Uri?>
) {
    val scope = rememberCoroutineScope()
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
                        .clickable {
                            if (imageUri != null) {
                                scope.launch {
                                    val user = viewModel.getUser()
                                    user?.let {
                                        it.avatar = imageUri.value.toString()
                                        viewModel.updateUser(user)
                                        viewModel.putUser()
                                    }
                                }
                            }
                            navController.navigate(NavRoute.Profile.route)
                        },
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
    imageUri: MutableState<Uri?>
) {
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri.value = uri
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(color = colorResource(id = R.color.LightLightGray)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        val context = LocalContext.current
        if (imageUri.value != null) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(imageUri.value)
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            if (userEntity?.avatar != null) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(userEntity.avatar?.let { Uri.parse(it) }).build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                )
            }
        }
        Text(
            text = "Редактировать",
            fontSize = 22.sp,
            fontFamily = FontFamily(Font(R.font.roboto_light)),
            color = Color.DarkGray,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable { launcher.launch("image/*") }
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
                                    text = if ((userEntity?.name == null) || (userEntity.name == "null") || (userEntity.name == "")) {
                                        "Добавьте информацию"
                                    } else {
                                        "${userEntity.name}"
                                    },
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
                                    text = if ((userEntity?.city == null) || (userEntity.city == "null") || (userEntity.city == "")) {
                                        "Добавьте информацию"
                                    } else {
                                        "${userEntity.city}"
                                    },
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
                                    text = if ((userEntity?.birthday == null) || (userEntity.birthday == "null") || (userEntity.birthday == "")) {
                                        "Добавьте информацию"
                                    } else {
                                        "${userEntity.birthday}"
                                    },
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
                                    text = if ((userEntity?.status == null) || (userEntity.status == "null") || (userEntity.status == "")) {
                                        "Добавьте информацию"
                                    } else {
                                        "${userEntity.status}"
                                    },
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