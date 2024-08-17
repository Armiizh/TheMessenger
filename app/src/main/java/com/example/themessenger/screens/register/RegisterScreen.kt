package com.example.themessenger.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
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
fun RegisterScreen(navController: NavHostController, viewModel: MainViewModel) {

    var name by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }
    val mobileNumber by remember {
        mutableStateOf("+79999999999")
    }

    Scaffold(
        modifier = Modifier
            .padding(horizontal = 36.dp)
            .padding(top = 72.dp),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
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

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = mobileNumber,
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
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                onClick = { navController.navigate(NavRoute.Register.route) },
                                shape = RoundedCornerShape(10.dp),
                                enabled = name.isNotEmpty() && username.isNotEmpty(),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = colorResource(id = R.color.green),
                                    contentColor = colorResource(id = R.color.white)
                                ),
                            ) {
                                Text(
                                    text = "Войти или зарегистрироваться",
                                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}