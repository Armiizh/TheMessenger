package com.example.themessenger.presentation.screens.profile.editScreens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.themessenger.R
import com.example.themessenger.data.room.model.UserEntity
import com.example.themessenger.domain.MainViewModel
import com.example.themessenger.presentation.MainActivity
import com.example.themessenger.presentation.navigation.NavRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBirthday(navController: NavHostController, viewModel: MainViewModel) {

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val userIdString = sharedPreferences.getString("user_id", "")
    var userEntity by remember { mutableStateOf<UserEntity?>(null) }
    val userDao = MainActivity.database.userDao()

    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    if (userIdString != null) {
        LaunchedEffect(Unit) {
            val userId = userIdString.toInt()
            userEntity = userDao.getUser(userId)
            val birthday = userEntity?.birthday
            day = birthday?.substring(8, 10).toString()
            month = birthday?.substring(5, 7).toString()
            year = birthday?.substring(0, 4).toString()
        }
    }

    val dayFiledValue =
        remember { mutableStateOf(TextFieldValue(text = "", selection = TextRange(0))) }
    val monthFiledValue =
        remember { mutableStateOf(TextFieldValue(text = "", selection = TextRange(0))) }
    val yearFiledValue =
        remember { mutableStateOf(TextFieldValue(text = "", selection = TextRange(0))) }
    dayFiledValue.value = TextFieldValue(
        text = day,
        selection = TextRange(day.length)
    )
    monthFiledValue.value = TextFieldValue(
        text = month,
        selection = TextRange(month.length)
    )
    yearFiledValue.value = TextFieldValue(
        text = year,
        selection = TextRange(year.length)
    )
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    val scope = rememberCoroutineScope()
    Scaffold(
        containerColor = colorResource(id = R.color.LightLightGray),
        topBar = {
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
                                    navController.navigate(NavRoute.EditProfile.route)
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
                                    scope.launch {
                                        val updateBirthday =
                                            "${yearFiledValue.value.text}-${monthFiledValue.value.text}-${dayFiledValue.value.text}"
                                        val zodiacSign =
                                            viewModel.determineZodiacSign(updateBirthday)
                                        userDao.updateUser(
                                            id = userEntity?.id,
                                            name = userEntity?.name,
                                            phone = userEntity?.phone,
                                            username = userEntity?.username,
                                            city = userEntity?.city,
                                            birthday = updateBirthday,
                                            zodiacSign = zodiacSign,
                                            status = userEntity?.status,
                                            avatar = userEntity?.avatar
                                        )
                                    }
                                    navController.navigate(NavRoute.EditProfile.route)
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
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .align(Alignment.CenterHorizontally),
                    value = dayFiledValue.value,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= 2) {
                            dayFiledValue.value = TextFieldValue(
                                newValue.text,
                                selection = TextRange(newValue.text.length)
                            )
                        }
                    },
                    label = { Text(text = "День рождения") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 24.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = KeyboardActions.Default.onNext,
                    )

                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    value = monthFiledValue.value,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= 2) {
                            monthFiledValue.value = TextFieldValue(
                                newValue.text,
                                selection = TextRange(newValue.text.length)
                            )
                        }
                    },
                    label = { Text(text = "Месяц рождения") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 24.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = KeyboardActions.Default.onNext
                    )
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    value = yearFiledValue.value,
                    onValueChange = { newValue ->
                        if (newValue.text.length <= 4) {
                            yearFiledValue.value = TextFieldValue(
                                newValue.text,
                                selection = TextRange(newValue.text.length)
                            )
                        }
                    },
                    label = { Text(text = "Год рождения") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = 24.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = KeyboardActions.Default.onDone
                    )
                )
            }
        }
    )
}