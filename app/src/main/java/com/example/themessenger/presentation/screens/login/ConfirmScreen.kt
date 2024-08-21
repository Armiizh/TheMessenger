package com.example.themessenger.presentation.screens.login

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.themessenger.presentation.MainViewModel
import com.example.themessenger.R
import com.example.themessenger.presentation.navigation.NavRoute
import com.example.themessenger.utils.Constants
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmScreen(navController: NavHostController, viewModel: MainViewModel) {

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
                    .padding(paddingValues)
                    .padding(top = 72.dp)
                    .padding(horizontal = 36.dp)
            ) {
                Texts()
                EnterCode(navController, viewModel)
            }
        }
    )
}

@Composable
private fun Texts() {
    Text(
        text = "Введите СМС-код",
        fontFamily = FontFamily(Font(R.font.roboto_medium)),
        fontSize = 36.sp,
        lineHeight = 36.sp
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EnterCode(navController: NavHostController, viewModel: MainViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .padding(horizontal = 50.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        var confirmCode by remember {
            mutableStateOf("")
        }
        val shake = remember { Animatable(0f) }
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        var incorrectCode by remember { mutableStateOf(false) }
        var textFieldColor by remember { mutableStateOf(Color.Black) } // Add this
        LaunchedEffect(incorrectCode) {
            if (incorrectCode) {
                textFieldColor = Color.Red
                for (i in 0..10) {
                    when (i % 2) {
                        0 -> shake.animateTo(5f, spring(stiffness = 100_000f))
                        else -> shake.animateTo(-5f, spring(stiffness = 100_000f))
                    }
                }
                shake.animateTo(0f)
                incorrectCode = false
                textFieldColor = Color.Black
            }
        }
        TextField(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .focusRequester(focusRequester)
                .offset(x = shake.value.roundToInt().dp, y = 0.dp),
            value = confirmCode,
            onValueChange = {
                if (it.length <= 6) {
                    confirmCode = it
                }
                if (it.length == 6 && it != Constants.CODE) {
                    incorrectCode = true
                } else if (it.length == 6) {
                    viewModel.setAuthCode(it)
                    viewModel.postAuthCode(navController)
                    navController.navigate(NavRoute.Register.route)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = textFieldColor
            ),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 36.sp,
                textAlign = TextAlign.Center,
                letterSpacing = 4.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular))
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}