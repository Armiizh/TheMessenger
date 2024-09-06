package com.example.themessenger.presentation.screens.profile

import android.net.Uri
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.themessenger.R
import com.example.themessenger.data.room.model.UserEntity
import com.example.themessenger.domain.MainViewModel
import com.example.themessenger.presentation.navigation.NavRoute
import kotlinx.coroutines.runBlocking

@Composable
fun ProfileScreen(navController: NavHostController, viewModel: MainViewModel) {

    val userEntityState = runBlocking { viewModel.getUser() }
    val userEntity by remember { mutableStateOf(userEntityState) }

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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
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
                            navController.navigate(NavRoute.Chats.route)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.left_arrow),
                        contentDescription = "",
                    )
                    Text(
                        text = "Чаты",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_medium))
                    )
                }
                Spacer(modifier = Modifier.weight(0.6f))
                Text(
                    text = "Профиль",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_bold)),
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { navController.navigate(NavRoute.EditProfile.route) }
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
        val context = LocalContext.current
        val imageUri = userEntity?.avatar?.let { Uri.parse(it) }
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(imageUri)
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
            Image(
                painter = painterResource(id = R.drawable.person),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
            )
        }
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
                    ProfileItem(
                        "Телефон",
                        "+${userEntity?.phone}" ?: "Добавьте информацию"
                    )
                }
                item {
                    ProfileItem("Никнейм", userEntity?.username ?: "Добавьте информацию")
                }
                item {
                    ProfileItem("Имя", userEntity?.name ?: "Добавьте информацию")
                }
                item {
                    ProfileItem("Город", userEntity?.city ?: "Добавьте информацию")
                }
                item {
                    ProfileItem("Дата рождения", userEntity?.birthday ?: "Добавьте информацию")
                }
                item {
                    ProfileItem("Знак зодиака", userEntity?.zodiacSign ?: "Добавьте информацию")
                }
                item {
                    ProfileItem("О себе", userEntity?.status ?: "Добавьте информацию")
                }
            }
        }
    }
}

@Composable
fun ProfileItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.roboto_bold)),
                color = Color.Black
            )
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = if (description != null || description != "null") {
                    description
                } else {
                    "Добавьте информацию"
                },
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                color = Color.Black
            )
            HorizontalDivider(
                modifier = modifier,
                thickness = 1.dp,
                color = Color.LightGray
            )
        }
    }
}