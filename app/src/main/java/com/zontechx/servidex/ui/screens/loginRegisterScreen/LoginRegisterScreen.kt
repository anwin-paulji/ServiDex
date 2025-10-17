package com.zontechx.servidex.ui.screens.loginRegisterScreen

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.AuthVM

@Composable
fun LoginRegisterScreen(rootNavController: NavHostController) {

    val TAG = "LoginRegisterScreen"

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
        ) {
            BackgroundImage()
            AppLogo(modifier = Modifier.align(Alignment.TopEnd))
            LoginRegisterArea(
                modifier = Modifier.align(Alignment.BottomCenter),
                rootNavController
            )
        }
    }
}

@Composable
fun BackgroundImage() {
    Image(
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds,
        painter = painterResource(id = R.drawable.on1),
        contentDescription = null
    )
}

@Composable
fun AppLogo(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.app_logo),
        contentDescription = null,
        modifier = modifier
            .padding(top = 16.dp, end = 16.dp)
            .width(200.dp)
            .height(80.dp)
    )
}

@Composable
fun LoginRegisterArea(
    modifier: Modifier,
    navController: NavController
) {
    val TAG = "LoginRegisterArea"

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val keyboardController = LocalSoftwareKeyboardController.current

    val authVM: AuthVM = viewModel()

    var mobileNumber by remember { mutableStateOf("") }

    var progressBar by remember { mutableStateOf<Boolean>(false) }

    fun loginRegisterApiRequest() {
        if (mobileNumber.length < 10) {
            Toast.makeText(context, "Please enter valid mobile number", Toast.LENGTH_SHORT).show()
            return
        }
        authVM.checkMobileNumberExist(mobileNumber);
    }

    fun onNewValueChange(newValue: String) {
        if (newValue.all { it.isDigit() }) {
            mobileNumber = newValue
        }
        if (newValue.length == 10) {
            keyboardController?.hide()
        }
    }

    LaunchedEffect(Unit) {
        authVM.loginResponse.collect { event ->
            when (val responseData = event) {
                is ApiResponse.Success -> {
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set("verificationId", responseData.data.data?.verificationId)
                        set("mobileNumber", responseData.data.data?.mobileNumber)
                    }
                    navController.navigate(Screen.OtpVerificationScreen.route)
                }
                is ApiResponse.Initial -> {
                    progressBar = false
                }

                is ApiResponse.Loading -> {
                    progressBar = true
                }
                is ApiResponse.Error -> {
                    progressBar = false
                    Toast.makeText(
                        context,
                        responseData.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /** Parent View*/
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(Color.White)
            .imePadding()
    ) {

        Text(
            text = AppConstant.LOGIN_REGISTER,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 35.dp,
                    top = 30.dp,
                    bottom = 10.dp
                ),
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 10.dp
                ),
            text = AppConstant.LOGIN_REGISTER_QUOT,
            style = TextStyle(
                fontSize = 12.sp,
                color = AppColor.HINT_COLOR,
            )
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
            prefix = {
                Text(text = AppConstant.COUNTRY_CODE)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            shape = Shapes().extraLarge,
            value = mobileNumber,
            placeholder = {
                Text(
                    text = AppConstant.LOGIN_PLACEHOLDER, style = TextStyle(
                        color = AppColor.HINT_COLOR
                    )
                )
            },
            onValueChange = { newValue ->
                onNewValueChange(newValue)
            },
            textStyle = TextStyle(letterSpacing = 12.sp, fontSize = 20.sp),
            colors = TextFieldDefaults.colors().copy(
                cursorColor = Color.Black,
                focusedIndicatorColor = AppColor.Primary,
                focusedContainerColor = AppColor.White,
                unfocusedContainerColor = AppColor.White
            )
        )

        /** Get OTP Bottom Button View */
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            if (progressBar) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 10.dp)
                        .width(30.dp)
                        .height(30.dp),
                    color = AppColor.Primary,
                    strokeWidth = 1.dp,
                    strokeCap = StrokeCap.Round
                )
            } else {


                AnimatedButton(
                    text = AppConstant.GET_OTP,
                    onClick = {
                        loginRegisterApiRequest()
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    textPaddingValues = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 15.dp,
                        top = 15.dp
                    )
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),

            text = AppConstant.CONTENT_1,
            style = TextStyle(
                fontSize = 12.sp,
                color = AppColor.HINT_COLOR,
            )
        )
    }
}