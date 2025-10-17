package com.zontechx.servidex.ui.screens.mainScreen.page.BookingScreen.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.common.api.Api
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.enum.BookingStatus
import com.zontechx.servidex.model.response.Bookings
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.model.sealed.UiEvent
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.components.RoundIconButton
import com.zontechx.servidex.ui.screens.mainScreen.LocalMainVM
import com.zontechx.servidex.ui.screens.mainScreen.page.BookingScreen.vm.BookingVM
import com.zontechx.servidex.ui.screens.mainScreen.vm.MainScreenVM
import com.zontechx.servidex.ui.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailsSheet(sheetState: SheetState, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = AppColor.White,
        dragHandle = {}) {
        ServiceDetailsSheetContent(sheetState = sheetState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ServiceDetailsSheetContent(sheetState: SheetState? = null) {

    val bookingVM: BookingVM = viewModel()
    val bookingData by bookingVM.selectedBookingDetails.collectAsState()

    fun handleSheetClose() {
        bookingVM.hideSheet()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppColor.white_2)
            .statusBarsPadding()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f, fill = true)
                .verticalScroll(rememberScrollState())
        ) {
            Toolbar { handleSheetClose() }
            BookingSummary(bookingData = bookingData)
            Spacer(modifier = Modifier.size(16.dp))
            ServiceDetails(bookingData = bookingData)
            Spacer(modifier = Modifier.size(16.dp))
            CustomerDetails(bookingData = bookingData)
            Spacer(modifier = Modifier.size(16.dp))
        }
        BottomBar()
    }
}

@Preview
@Composable
fun Toolbar(onClose: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = AppConstant.SERVICE_DETAILS, style = TextStyle(
                color = AppColor.Black, fontSize = 20.sp, fontFamily = FontFamily(
                    Font(R.font.roboto_semibold)
                )
            )
        )
        RoundIconButton(
            backgroundColor = AppColor.White,
            iconTint = AppColor.Black,
            iconRes = R.drawable.ic_close,
            modifier = Modifier.clickable { onClose() })
    }
}

@Preview
@Composable
fun BottomBar() {
    val TAG = "BottomBar"
    val context = LocalContext.current
    val bookingVM: BookingVM = viewModel()
    val bookingStatusUpdateResponse by bookingVM.bookingStatusUpdateResponse.collectAsState()
    val bookingData by bookingVM.selectedBookingDetails.collectAsState()
    var progress by remember { mutableStateOf(false) }
    var positiveText = ""
    var showPositive = false

    when (bookingData?.bookingStatus) {
        BookingStatus.COMPLETED.value -> {
            if(bookingData?.isServiceRatingCompleted == false){
                positiveText = "⭐ Rate Customer"
                showPositive = true
            } else {
                showPositive = false
            }
        }

        BookingStatus.CANCELLED.value -> {
            showPositive = false
        }

        else -> {
            positiveText = "Cancel Service"
            showPositive = true
        }
    }

    LaunchedEffect(Unit) {
        bookingVM.reviewRatingResponse.collect {
            when (it) {
                is ApiResponse.Loading -> {
                    progress = true
                }

                is ApiResponse.Success -> {
                    progress = false
                    Toast.makeText(context, "Rating Submitted", Toast.LENGTH_SHORT).show()
                }

                is ApiResponse.Error -> {
                    progress = false
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                else -> {
                    progress = false
                }
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 10.dp)
    ) {
        if (bookingStatusUpdateResponse is ApiResponse.Loading || progress) {
            CircularProgressIndicator(color = AppColor.Primary, strokeWidth = 4.dp)
        } else {
            if (showPositive) {
                AnimatedButton(
                    modifier = Modifier.fillMaxWidth(),
                    paddingTop = 5.dp,
                    paddingStart = 0.dp,
                    paddingEnd = 0.dp,
                    textColor = AppColor.White,
                    text = positiveText,
                    onClick = { bookingVM.handleServiceDetailsNegativeAction() })
            }
        }
    }
}

@Preview
@Composable
fun BookingSummary(bookingData: Bookings? = null) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = AppColor.White)
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = AppConstant.BOOKING_SUMMARY, style = TextStyle(
                color = AppColor.Black, fontSize = 20.sp, fontFamily = FontFamily(
                    Font(R.font.roboto_semibold)
                )
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        DetailsItem(title = "Booking ID", value = bookingData?._id ?: "")
        DetailsItem(
            title = "Service Type",
            icon = R.drawable.plumbing_service_icon,
            value = bookingData?.serviceDetails?.serviceName ?: ""
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailsItem(
                title = "Booking Date",
                icon = R.drawable.calender_80,
                value = bookingData?.serviceDate ?: ""
            )
            Text(
                text = bookingData?.bookingStatus ?: "",
                style = TextStyle(
                    color = AppColor.White, fontSize = 16.sp, fontFamily = FontFamily(
                        Font(R.font.roboto_medium)
                    )
                ),
                modifier = Modifier
                    .background(AppColor.Primary, shape = RoundedCornerShape(8.dp))
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            )
        }
    }
}

@Preview
@Composable
fun ServiceDetails(bookingData: Bookings? = null) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = AppColor.White)
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = AppConstant.SERVICE_DETAILS, style = TextStyle(
                color = AppColor.Black, fontSize = 20.sp, fontFamily = FontFamily(
                    Font(R.font.roboto_semibold)
                )
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            DetailsItem(
                modifier = Modifier.weight(1f),
                title = "Category",
                value = bookingData?.serviceDetails?.category?.categoryName ?: ""
            )
            DetailsItem(
                modifier = Modifier.weight(1f),
                title = "Sub-Category",
                value = bookingData?.serviceDetails?.subCategory?.subCategoryName ?: ""
            )
        }
        DetailsItem(title = "Price", value = "₹100")
        DetailsItem(title = "Estimated Duration", value = "4 hours")
    }
}

@Preview
@Composable
fun CustomerDetails(bookingData: Bookings? = null) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = AppColor.White)
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Text(
            text = AppConstant.CUSTOMER_DETAILS, style = TextStyle(
                color = AppColor.Black, fontSize = 20.sp, fontFamily = FontFamily(
                    Font(R.font.roboto_semibold)
                )
            )
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            DetailsItem(
                modifier = Modifier.weight(1f),
                icon = R.drawable.profile,
                title = AppConstant.CUSTOMER_NAME,
                value = bookingData?.contactDetails?.name ?: "N/A",
            )
            DetailsItem(
                modifier = Modifier.weight(1f),
                icon = R.drawable.ic_call_outline,
                title = AppConstant.MOBILE_NUMBER,
                value = bookingData?.contactDetails?.mobileNumber ?: "N/A"
            )
        }

        DetailsItem(title = "Address", value = "123 Main St, City, Country")
        Spacer(modifier = Modifier.height(12.dp))
        DetailsItem(title = "Special Instructions", value = "(No specific instructions)")

        CallCustomer()
    }
}

@Preview
@Composable
fun CallCustomer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = AppColor.PrimaryBG)
            .padding(top = 12.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_call_outline),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = AppColor.Primary
        )
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Call Customer", style = TextStyle(
                color = AppColor.Primary,
                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                fontSize = 14.sp
            )
        )
    }
}

@Preview
@Composable
fun DetailsItem(
    modifier: Modifier = Modifier,
    noIcon: Boolean = false,
    icon: Int = R.drawable.location,
    title: String = "Scheduled Date",
    value: String = "Sep 04, 2025"
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (!noIcon) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = AppColor.gray_4
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = title, style = TextStyle(
                    color = AppColor.gray_4, fontSize = 14.sp, fontFamily = FontFamily(
                        Font(R.font.roboto_regular)
                    )
                )
            )
            Text(
                text = value, style = TextStyle(
                    color = AppColor.Black, fontSize = 16.sp, fontFamily = FontFamily(
                        Font(R.font.roboto_semibold)
                    )
                )
            )
        }
    }
}