package com.example.themessenger.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.themessenger.MainViewModel
import com.example.themessenger.R
import com.example.themessenger.navigation.NavRoute
import network.chaintech.cmpcountrycodepicker.model.CountryDetails
import network.chaintech.cmpcountrycodepicker.ui.CountryPickerBasicTextField

@Composable
fun LoginScreen(navController: NavHostController, viewModel: MainViewModel) {
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
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(top = 72.dp)
            ) {
                var mobileNumber by remember {
                    mutableStateOf("")
                }
                val selectedCountryState: MutableState<CountryDetails?> = remember {
                    mutableStateOf(null)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp)
                ) {
                    Texts()
                    CountryPickerBasicTextField(
                        mobileNumber = mobileNumber,
                        defaultCountryCode = "ru",
                        onMobileNumberChange = {
                            if (it.length <= 10) {
                                mobileNumber = it
                            }
                        },
                        onCountrySelected = {
                            selectedCountryState.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        defaultPaddingValues = PaddingValues(6.dp),
                        showCountryFlag = true,
                        showCountryPhoneCode = true,
                        showCountryName = false,
                        showCountryCode = false,
                        showArrowDropDown = true,
                        spaceAfterCountryFlag = 8.dp,
                        spaceAfterCountryPhoneCode = 6.dp,
                        spaceAfterCountryName = 6.dp,
                        spaceAfterCountryCode = 6.dp,
                        label = {
                            Text(
                                text = "Ваш номер",
                                fontFamily = FontFamily(Font(R.font.roboto_regular))
                            )
                        },
                        focusedBorderThickness = 2.dp,
                        unfocusedBorderThickness = 1.dp,
                        shape = RoundedCornerShape(10.dp),
                        verticalDividerColor = Color(0XFFDDDDDD),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black
                        )
                    )
                }

                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 36.dp)
                        .height(48.dp),
                    onClick = { navController.navigate(NavRoute.Chats.route) },
                    enabled = mobileNumber.isNotEmpty() && mobileNumber.length == 10,
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
        }
    )
}

@Composable
private fun Texts() {
    Text(
        text = "Введите",
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        fontSize = 36.sp,
        lineHeight = 36.sp
    )
    Text(
        text = "номер телефона",
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        fontSize = 36.sp,
        lineHeight = 36.sp
    )
    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = "Чтобы войти или стать",
        fontFamily = FontFamily(Font(R.font.roboto_light)),
        fontSize = 16.sp,
        lineHeight = 16.sp
    )
    Text(
        text = "пользователем мессенджера",
        fontFamily = FontFamily(Font(R.font.roboto_light)),
        fontSize = 16.sp,
        lineHeight = 16.sp
    )
}