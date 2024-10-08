package com.example.themessenger.presentation.screens.profile.editScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.themessenger.R
import com.example.themessenger.domain.MainViewModel
import com.example.themessenger.presentation.navigation.NavRoute
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditName(navController: NavHostController, viewModel: MainViewModel) {

    val userEntityState = runBlocking { viewModel.getUser() }
    val userEntity by remember { mutableStateOf(userEntityState) }

    var name by remember { mutableStateOf(userEntity?.name ?: "") }
    val nameFieldValue = remember { mutableStateOf(TextFieldValue(text = "", selection = TextRange(0))) }
    nameFieldValue.value = TextFieldValue(
        text = name,
        selection = TextRange(name.length)
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
                                        val user = viewModel.getUser()
                                        name = nameFieldValue.value.text
                                        user?.let {
                                            it.name = name
                                            viewModel.updateUser(user)
                                        }
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
                    .fillMaxSize()
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = nameFieldValue.value,
                    onValueChange = { newValue ->
                        nameFieldValue.value = TextFieldValue(
                            text = newValue.text,
                            selection = TextRange(newValue.text.length)
                        )
                    },
                    label = { Text(text = "Имя") },
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
                        keyboardType = KeyboardType.Text
                    )
                )
            }
        }
    )
}