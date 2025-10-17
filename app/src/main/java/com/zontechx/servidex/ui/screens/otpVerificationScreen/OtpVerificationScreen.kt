package com.zontechx.servidex.ui.screens.otpVerificationScreen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.service.local.AppStorage
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.OtpVerificationScreenVM
import com.zontechx.servidex.utils.InAppStore

@Composable
fun OtpVerificationScreen(rootNavController: NavHostController) {

    val TAG = "OtpVerificationScreen"

    val context = LocalContext.current
    val activity = context as ComponentActivity

    val registerId =
        rootNavController.previousBackStackEntry?.savedStateHandle?.get<String>("verificationId")

    val mobileNumber =
        rootNavController.previousBackStackEntry?.savedStateHandle?.get<String>("mobileNumber")

    val vm: OtpVerificationScreenVM = viewModel()

    val response by vm.verifyOtpResponse.collectAsState()

    BackHandler(enabled = true) { rootNavController.popBackStack() }

    LaunchedEffect(response) {
        when (response) {
            null -> {}
            is ApiResponse.Initial -> {
                Log.e(TAG, "Initial ****")
            }

            is ApiResponse.Loading -> {
                Log.e(TAG, "Loading ****")
            }

            is ApiResponse.Success -> {
                val responseData = (response as ApiResponse.Success).data
                InAppStore.saveString(activity, AppKey.FCM_TOKEN, responseData.data?.token ?: "");
                InAppStore.saveBoolean(activity, AppKey.IS_LOGIN, true);
                Log.e(
                    TAG,
                    "isProfileCompleted" + responseData.data?.userData!!.isProfileCompleted.toString()
                )
                InAppStore.saveBoolean(
                    activity,
                    AppKey.IS_PROFILE_COMPLETE,
                    responseData.data.userData.isProfileCompleted
                )
                AppStorage.setToken(activity, responseData.data.token)

                rootNavController.navigate(Screen.LocationScreen.route)
            }

            is ApiResponse.Error -> {
                val error = (response as ApiResponse.Error).message
                Log.e(TAG, "Failed ${error}")
            }
        }
    }


    if (registerId != null && mobileNumber != null) {
        OtpVerificationScreen_Main(registerId = registerId, mobileNumber = mobileNumber)
    }
}

@Composable
fun OtpVerificationScreen_Main(registerId: String, mobileNumber: String) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
        ) {

            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.on2),
                contentDescription = null
            )

            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .width(200.dp)
                    .height(80.dp)
                    .align(Alignment.TopEnd)
            )

            OtpArea(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .align(Alignment.BottomCenter)
                    .background(Color.White)
                    .imePadding(),
                registerId = registerId,
                mobileNumber = mobileNumber
            )
        }
    }
}

@Composable
fun OtpArea(
    modifier: Modifier,
    registerId: String,
    mobileNumber: String
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val vm: OtpVerificationScreenVM = viewModel()

    var progress by remember { mutableStateOf<Boolean>(false) }

    var enableButton = remember { mutableStateOf(false) }

    var otpValue = remember { mutableStateOf("") }

    val response by vm.verifyOtpResponse.collectAsState()

    LaunchedEffect(response) {
        when (response) {
            null -> {}
            is ApiResponse.Initial -> {}

            is ApiResponse.Loading -> {
                progress = true
            }

            is ApiResponse.Success -> {
                progress = false
            }

            is ApiResponse.Error -> {
                progress = false
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 30.dp,
                    bottom = 10.dp
                ),
            text = AppConstant.ENTER_CODE,
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
                    end = 20.dp
                ),
            text = AppConstant.CONTENT_3,
            style = TextStyle(
                fontSize = 12.sp,
                color = AppColor.HINT_COLOR,
            )
        )

        OtpFields { otp ->
            otpValue.value = otp
            if (otp.length == 4) {
                enableButton.value = true
                keyboardController?.hide()
            } else {
                enableButton.value = false
            }
        }

        VerifyButton(
            progress = progress,
            enableButton = enableButton.value,
            onVerifyClick = {
                vm.verifyOtp(registerId, mobileNumber, otpValue.value)
            },
            onResendClick = {})

        Text(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 20.dp
                ),

            text = AppConstant.CONTENT_2,
            style = TextStyle(
                fontSize = 12.sp,
                color = AppColor.HINT_COLOR,
            )
        )
    }


    fun onBackPress(
        index: Int,
        otpValue: MutableList<String>,
        focusRequester: List<FocusRequester>
    ) {
        if (index > 0) {
            focusRequester[index - 1].requestFocus()
            otpValue[index - 1] = ""
        }
    }

}

@Composable
fun VerifyButton(
    progress: Boolean = false,
    enableButton: Boolean = false,
    onVerifyClick: () -> Unit = {},
    onResendClick: () -> Unit = {}
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Didn't receive the code? ",
                modifier = Modifier.align(Alignment.CenterVertically),
                style = TextStyle(
                    fontSize = 12.sp,
                    color = AppColor.HINT_COLOR
                )
            )
            Text(
                text = "Resend", color = AppColor.Primary,
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier.clickable {
                    if (!progress) onResendClick()
                })
        }

        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(horizontal = 30.dp),
            modifier = Modifier
                .width(140.dp)
                .padding(
                    bottom = 10.dp
                )
                .clip(RoundedCornerShape(28.dp))
                .background( color = if (enableButton) { AppColor.Primary } else { AppColor.gray } ),
            enabled = !progress,
            onClick = {
                onVerifyClick()
            })
        {
            if (progress) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = AppConstant.VERIFY_OTP,
                    color = if (enableButton) Color.White else Color.Gray,
                )
            }
        }
    }
}

@Composable
fun OtpFields(onOtpValueChange: (otp: String) -> Unit) {
    val otpValue = remember { mutableStateOf("") }

    OutlinedTextField(
        value = otpValue.value,
        onValueChange = { newValue ->
            if (newValue.length <= 6) {
                otpValue.value = newValue
                onOtpValueChange(newValue)
            }
        },
        label = { Text(text = "Enter OTP", style = TextStyle(color = Color.Black)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            letterSpacing = 20.sp,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = Shapes().extraLarge,
        colors = TextFieldDefaults.colors().copy(
            cursorColor = Color.Black,
            focusedIndicatorColor = AppColor.Primary,
            focusedContainerColor = AppColor.White,
            unfocusedContainerColor = AppColor.White
        )
    )
}

@Preview
@Composable
fun OtpFieldsPreview() {
    Column(
        modifier = Modifier.background(Color.White)
    ) {
        OtpFields {}
        VerifyButton()
    }
}