package com.zontechx.servidex.ui.provider.providerDashboard.page.providerBooking.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.model.enum.BookingStatus
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.screens.mainScreen.LocalMainVM
import com.zontechx.servidex.ui.screens.mainScreen.page.BookingScreen.vm.BookingVM
import com.zontechx.servidex.ui.screens.mainScreen.vm.MainScreenVM
import com.zontechx.servidex.ui.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit = {},
    onPositiveClick: () -> Unit = {},
    onNegativeClick: () -> Unit = {}
) {

    val bookingVM: BookingVM = viewModel()
    val sheetUiState by bookingVM.confirmationSheetState.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(16.dp),
//        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        containerColor = AppColor.White,
        dragHandle = {}
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Title
            Text(
                text = sheetUiState.title,
                style = TextStyle(
                    fontSize = 22.sp,
                    color = AppColor.Black,
                    fontFamily = FontFamily(Font(R.font.roboto_bold))
                )
            )

            // Description
            Text(
                text = sheetUiState.description,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = AppColor.gray_4,
                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                )
            )
            if (sheetUiState.newBookingStatus != BookingStatus.REJECTED && sheetUiState.newBookingStatus != BookingStatus.CANCELLED) {
                Spacer(modifier = Modifier.height(30.dp))
            } else {
                ReasonField(
                    modifier = Modifier.weight(1f, fill = true),
                    sheetUiState.newBookingStatus
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Negative Button (optional)
                sheetUiState.negativeText?.let {
                    AnimatedButton(
                        text = it,
                        paddingStart = 0.dp,
                        paddingEnd = 0.dp,
                        textColor = AppColor.Primary,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onNegativeClick,
                        backgroundColor = AppColor.White,
                        buttonBorderColor = AppColor.Primary
                    )
                }

                // Positive Button
                AnimatedButton(
                    text = sheetUiState.positiveText,
                    paddingStart = 0.dp,
                    paddingEnd = 0.dp,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onPositiveClick
                )
            }
        }
    }
}

@Preview
@Composable
fun ReasonField(
    modifier: Modifier = Modifier,
    bookingStatus: BookingStatus = BookingStatus.CANCELLED
) {

    val bookingVM: BookingVM = viewModel()

    val selectedReason by bookingVM.selectedReason.collectAsState()

    val cancellationReasons = listOf(
        "Customer not responding",
        "Wrong address or unreachable location",
        "Customer requested to cancel",
        "Schedule conflict — can't attend at the booked time",
        "Emergency — unable to reach customer location",
        "Service not available in this area",
        "Pricing or service mismatch",
        "Duplicate booking by customer",
        "Tools or materials unavailable",
        "Other reason"
    )

    val rejectionReasons = listOf(
        "Not available at the requested time",
        "Service area is too far",
        "Customer details are unclear",
        "Service type not supported",
        "Pricing mismatch",
        "High workload — unable to take new bookings",
        "Incomplete booking information",
        "Other reason"
    )

    val reasonList = when (bookingStatus) {
        BookingStatus.CANCELLED -> cancellationReasons
        BookingStatus.REJECTED -> rejectionReasons
        else -> emptyList()
    }

    Column(modifier = modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = selectedReason,
            onValueChange = { bookingVM.onReasonChange(it) },
            label = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(top = 5.dp, bottom = 5.dp),
            placeholder = { Text("Reason for the action...", style = TextStyle(fontSize = 14.sp)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_reason),
                    contentDescription = null,
                    tint = AppColor.gray_4,
                    modifier = Modifier.size(20.dp)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(50.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = AppColor.white_3,
                focusedContainerColor = AppColor.white_3,
                focusedBorderColor = AppColor.white_3,
                unfocusedBorderColor = AppColor.white_3
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(reasonList) { reason ->
                Text(
                    text = reason,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { bookingVM.onReasonChange(reason) }
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                )
            }
        }
    }
}
