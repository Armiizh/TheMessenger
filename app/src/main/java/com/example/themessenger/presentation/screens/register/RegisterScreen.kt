package com.example.themessenger.presentation.screens.register

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.themessenger.R
import com.example.themessenger.domain.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController, viewModel: MainViewModel) {

    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    val usernameRegex = Regex("^[A-Za-z0-9-_]{1,}$")

    fun validateUsername(username: String): Boolean {
        return usernameRegex.matches(username)
    }

    Scaffold(
        content = { paddingValues ->
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.bg4),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 36.dp)
                    .padding(top = 72.dp),
            ) {
                Texts()
                Column(
                    modifier = Modifier.padding(top = 100.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(
                                color = colorResource(id = R.color.TopAppBarColor),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Number(viewModel)
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = name,
                                onValueChange = {
                                    name = it
                                },
                                label = {
                                    Text(
                                        text = "Ваше имя",
                                        fontFamily = FontFamily(Font(R.font.roboto_regular))
                                    )
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent,
                                ),
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                                )
                            )
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = username,
                                onValueChange = {
                                    username = it
                                    if (!validateUsername(it)) {
                                        usernameError = "Недопустимый символ в имени пользователя"
                                    } else {
                                        usernameError = ""
                                    }
                                },
                                label = {
                                    Text(
                                        text = "Ваш никнейм",
                                        fontFamily = FontFamily(Font(R.font.roboto_regular))
                                    )
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    errorIndicatorColor = Color.Transparent,
                                ),
                                singleLine = true,
                                textStyle = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                                )
                            )
                            Button(viewModel, name, username, navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun Button(
    viewModel: MainViewModel,
    name: String,
    username: String,
    navController: NavHostController
) {
    ElevatedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        onClick = {
            viewModel.name = name
            viewModel.setUserName(username)
            Log.d("Check", "name is ${viewModel.name}")
            viewModel.register(navController)
        },
        shape = RoundedCornerShape(10.dp),
        enabled = name.isNotEmpty() && username.isNotEmpty(),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(16.dp)
    ) {
        Text(
            text = "Войти или зарегистрироваться",
            fontFamily = FontFamily(Font(R.font.roboto_regular))
        )
    }
}

@Composable
private fun Texts() {
    Text(
        text = "Привет!",
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        fontSize = 36.sp,
        lineHeight = 36.sp
    )
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = "Cоздай свой аккаунт",
        fontFamily = FontFamily(Font(R.font.roboto_light)),
        fontSize = 16.sp,
        lineHeight = 16.sp
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Number(viewModel: MainViewModel) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = viewModel.getMobileNumber(),
        onValueChange = {},
        label = {
            Text(
                text = "Ваш номер",
                fontFamily = FontFamily(Font(R.font.roboto_regular))
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
        singleLine = true,
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.roboto_regular))
        ),
        readOnly = true
    )
}