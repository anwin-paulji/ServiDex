package com.zontechx.servidex.ui.screens.booking

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.data.static.AppConstant.ADDITIONAL_NOTES
import com.zontechx.servidex.data.static.AppConstant.ADDITIONAL_NOTES_DESCRIPTION
import com.zontechx.servidex.data.static.AppConstant.ADDITIONAL_NOTES_HINT
import com.zontechx.servidex.data.static.AppConstant.SERVICE_ADDERSS
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.provider.addServiceScreen.component.ServiceArea
import com.zontechx.servidex.ui.screens.booking.components.BottomBar
import com.zontechx.servidex.ui.screens.booking.components.ConfirmBookingSheet
import com.zontechx.servidex.ui.screens.booking.components.ContactDetails
import com.zontechx.servidex.ui.screens.booking.components.SelectDateAndTime
import com.zontechx.servidex.ui.screens.booking.components.TopBar
import com.zontechx.servidex.ui.screens.booking.vm.BookingVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM

@Preview
@Composable
fun BookingScreen(
    rootNavController: NavHostController = rememberNavController(),
    serviceJsonData: String = "",
    screenSource: String = "",
) {

    val TAG = "BookingScreen"

    val context = LocalContext.current
    val sharedVM: SharedVM = LocalSharedViewModel.current
    val userData by sharedVM.userData.collectAsState()
    val vm: BookingVM = viewModel()

    val additionalNotes by vm.additionalNotes.collectAsState()

    val handleBackPress: () -> Unit = {
        rootNavController.popBackStack()
    }

    fun handleBookNowPress() {
        val currentUserData = userData
        when {
            currentUserData == null -> {
                Log.e(TAG,"#2")
                Toast.makeText(
                    context,
                    "Please restart the app, some issue with user data",
                    Toast.LENGTH_SHORT
                ).show()
            }

            currentUserData.isProfileCompleted == true -> {
                Log.e(TAG,"#2")
                ConfirmBookingSheet.show()
            }

            else -> {
                Log.e(TAG,"#3")
                sharedVM.showProfileCompletionBottomSheet(true)
            }
        }
    }

    LaunchedEffect(Unit) {
        /** Setting @see serviceDetails data from previous screen to current screen viewModel*/
        vm.setServiceJson(serviceJsonData)
    }

    LaunchedEffect(Unit) {
        vm.bookingResponse.collect { bookingResponse ->
            when (bookingResponse) {
                is ApiResponse.Success -> {
                    Toast.makeText(context, "Booking Successful", Toast.LENGTH_SHORT).show()
                    rootNavController.popBackStack()
                }

                is ApiResponse.Error -> {
                    Log.e(TAG, bookingResponse.message)
                    Toast.makeText(context, bookingResponse.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    Scaffold(
        containerColor = AppColor.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Fixed TopBar below status bar
            TopBar(onClickBack = { handleBackPress() })

            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                ServiceArea(
                    title = SERVICE_ADDERSS,
                    description = SERVICE_ADDERSS)

                SelectDateAndTime()

                CustomTextField(
                    title = ADDITIONAL_NOTES,
                    hint = ADDITIONAL_NOTES_HINT,
                    description = ADDITIONAL_NOTES_DESCRIPTION,
                    minLines = 3,
                    backgroundColor = AppColor.white_2,
                    singleLine = false,
                    textValue = additionalNotes,
                    onValueChange = { vm.onAdditionalNotesChange(it) })

                ContactDetails(vm = vm)
            }

            // BottomBar at the bottom of the Column
            BottomBar { handleBookNowPress() }
        }
    }

    ConfirmBookingSheet.Register(onClickButton1 = {
        vm.onBookServiceClick()
    }, onClickButton2 = {})
}


@Composable
fun CustomTextField(
    title: String = "",
    hint: String = "",
    description: String = "",
    textValue: String,
    minLines: Int = 1,
    singleLine: Boolean = true,
    backgroundColor: Color = AppColor.White,
    onValueChange: (String) -> Unit,
) {

    var textFieldModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 5.dp)
            .fillMaxWidth()

    var titleModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 2.dp)
            .fillMaxWidth()

    var descriptionModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 15.dp)
            .fillMaxWidth()

    var titleTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

    var hintTextStyle =
        TextStyle(color = AppColor.gray_4, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var descritptionTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 12.sp)

    var inputTextStyle =
        TextStyle(color = AppColor.Black, fontWeight = FontWeight.W600, fontSize = 18.sp)

    Column(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = backgroundColor)
    ) {
        Text(text = title, modifier = titleModifier, style = titleTextStyle)

        TextField(
            value = textValue,
            onValueChange = onValueChange,
            placeholder = { Text(text = hint, style = hintTextStyle) },
            singleLine = singleLine,
            minLines = minLines,
            textStyle = inputTextStyle,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppColor.gray_2,
                unfocusedContainerColor = AppColor.gray_2,
                focusedIndicatorColor = AppColor.gray_2,
                unfocusedIndicatorColor = AppColor.gray_2,
                cursorColor = AppColor.Black
            ),
            modifier = textFieldModifier
        )

        Text(text = description, modifier = descriptionModifier, style = descritptionTextStyle)
    }
}