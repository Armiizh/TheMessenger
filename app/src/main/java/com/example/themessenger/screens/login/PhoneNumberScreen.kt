package com.example.themessenger.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.themessenger.R
import network.chaintech.cmpcountrycodepicker.model.CountryDetails
import network.chaintech.cmpcountrycodepicker.ui.CountryPickerBasicTextField

@Composable
fun PhoneNumberScreen() {
    Scaffold(
        modifier = Modifier.padding(horizontal = 36.dp).padding(top = 36.dp),
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                Texts()

                val selectedCountryState: MutableState<CountryDetails?> = remember {
                    mutableStateOf(null)
                }
                var mobileNumber by remember {
                    mutableStateOf("")
                }

                CountryPickerBasicTextField(
                    mobileNumber = mobileNumber,
                    defaultCountryCode = "ru",
                    onMobileNumberChange = {
                        mobileNumber = it
                    },
                    onCountrySelected = {
                        selectedCountryState.value = it
                    },
                    modifier = Modifier.fillMaxWidth()
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
                        Text(text = "Ваш номер")
                    },
                    focusedBorderThickness = 2.dp,
                    unfocusedBorderThickness = 1.dp,
                    shape = RoundedCornerShape(10.dp),
                    verticalDividerColor = Color(0XFFDDDDDD),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0XFFDDDDDD),
                        unfocusedBorderColor = Color(0XFFDDDDDD)
                    )
                )
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