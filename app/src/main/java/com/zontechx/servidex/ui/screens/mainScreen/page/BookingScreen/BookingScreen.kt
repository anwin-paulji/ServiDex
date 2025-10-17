package com.zontechx.servidex.ui.screens.mainScreen.page.BookingScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import com.zontechx.servidex.R
import com.zontechx.servidex.model.enum.BookingStatus
import com.zontechx.servidex.model.response.Bookings
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.components.RatingReviewSheet
import com.zontechx.servidex.ui.screens.mainScreen.LocalMainVM
import com.zontechx.servidex.ui.components.RouteInfoComponent
import com.zontechx.servidex.ui.screens.mainScreen.page.BookingScreen.component.ConfirmationSheet
import com.zontechx.servidex.ui.screens.mainScreen.page.BookingScreen.component.ServiceDetailsSheet
import com.zontechx.servidex.ui.screens.mainScreen.page.BookingScreen.vm.BookingVM
import com.zontechx.servidex.ui.screens.mainScreen.vm.MainScreenVM
import com.zontechx.servidex.ui.theme.AppColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen() {

    val scope = rememberCoroutineScope()
    val refreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val mainScreenVM: MainScreenVM = LocalMainVM.current
    val bookingVM: BookingVM = viewModel()
    val context = LocalContext.current
    val bookings by bookingVM.bookingsUiState.collectAsState()
    val showServiceDetailsSheet by bookingVM.showServiceDetailsSheet.collectAsState()
    val bookingStatusUpdateResponse by bookingVM.bookingStatusUpdateResponse.collectAsState()
    val confirmationSheetUiState by bookingVM.confirmationSheetState.collectAsState()
    val showReviewRatingSheet by bookingVM.showReviewRatingSheet.collectAsState()

    val serviceDetailsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val confirmationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val reviewRatingSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val onRefresh: () -> Unit = {
        isRefreshing = true
        bookingVM.loadInitial()
    }

    LaunchedEffect(Unit) {
        bookingVM.bookingsUiState.collect { it ->
            when (it) {
                is ApiResponse.Success -> {
                    isRefreshing = false
                }

                is ApiResponse.Error -> {
                    isRefreshing = false
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(bookingStatusUpdateResponse) {
        when (val response = bookingStatusUpdateResponse) {
            is ApiResponse.Success -> {
                Toast.makeText(context, response.data.message, Toast.LENGTH_SHORT).show()
            }

            is ApiResponse.Error -> {
                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }

    LaunchedEffect(showServiceDetailsSheet) {
        if (showServiceDetailsSheet) {
            serviceDetailsSheetState.show()
        } else {
            serviceDetailsSheetState.hide()
        }
    }

    LaunchedEffect(confirmationSheetUiState) {
        if (confirmationSheetUiState.visible) {
            confirmationSheetState.show()
        } else {
            confirmationSheetState.hide()
        }
    }

    LaunchedEffect(showReviewRatingSheet) {
        if (showReviewRatingSheet) {
            reviewRatingSheetState.show()
        } else {
            reviewRatingSheetState.hide()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            state = refreshState,
            onRefresh = onRefresh,
            modifier = Modifier
                .background(AppColor.White)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColor.White)
                    .padding(innerPadding)
            ) {
                Toolbar()
                ToolBarList()
                BookingList()
            }
        }
    }

    if (showServiceDetailsSheet) {
        ServiceDetailsSheet(sheetState = serviceDetailsSheetState) {
            bookingVM.hideSheet()
        }
    }

    if (confirmationSheetUiState.visible) {
        ConfirmationSheet(
            sheetState = confirmationSheetState,
            onDismiss = { bookingVM.closeConfirmationSheet() },
            onPositiveClick = {
                scope.launch {
                    confirmationSheetState.hide()
                    bookingVM.onPositiveConfirmationAction()
                }
            },
            onNegativeClick = {
                scope.launch {
                    confirmationSheetState.hide()
                    bookingVM.closeConfirmationSheet()
                }
            })
    }
    if (showReviewRatingSheet) {
        RatingReviewSheet(
            sheetState = reviewRatingSheetState,
            onDismiss = { bookingVM.closeReviewRatingSheet() },
            onRatingSubmit = { rating, review ->
                scope.launch {
                    val bookingId = bookingVM.selectedBookingDetails.value!!._id
                    reviewRatingSheetState.hide()
                    bookingVM.onRatingSubmit(bookingId = bookingId, rating = rating, review = review)
                }
            }
        )
    }
}

@Preview
@Composable
fun Toolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.White)
    ) {
        Text(
            text = "Booking", modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            style = TextStyle(
                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.roboto_semibold))
            )
        )
    }
}

@Preview
@Composable
fun BookingList() {
    val listState = rememberLazyListState()
    val bookingVM: BookingVM = viewModel()
    val bookings by bookingVM.bookingsList.collectAsState()
    val hasMore by bookingVM.hasMore
    val isLoadingMore by bookingVM.isLoadingMore


    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White)
    ) {
        items(bookings, key = { it._id }) { booking ->
            BookingItem(booking) {
                bookingVM.onOrderClick(booking._id)
            }
        }

        if (isLoadingMore) {
            item {
                CircularProgressIndicator(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }

    // 👇 Detect scroll end and load more
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex == bookings.lastIndex && hasMore && !isLoadingMore) {
                    bookingVM.loadMore()
                }
            }
    }
}

@Preview
@Composable
fun ToolBarList() {

    val TAG = "ToolBarList"
    val bookingVM: BookingVM = viewModel()
    val toolBarData = listOf("All", "Ongoing", "Completed", "Cancelled")

    var selected by remember { mutableStateOf(toolBarData.first()) } // default "All"

    fun onChipSelected(item: String) {
        selected = item
        when (item) {
            "All" -> bookingVM.loadInitial(BookingStatus.ALL)
            "Ongoing" -> bookingVM.loadInitial(BookingStatus.IN_PROGRESS)
            "Completed" -> bookingVM.loadInitial(BookingStatus.COMPLETED)
            "Cancelled" -> bookingVM.loadInitial(BookingStatus.CANCELLED)
        }
    }

    LaunchedEffect(Unit) { bookingVM.loadInitial(BookingStatus.ALL) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(toolBarData) { item ->
                FilterChip(
                    selected = selected == item,
                    onClick = { onChipSelected(item) },
                    label = { Text(item) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = AppColor.White,              // background when not selected
                        labelColor = AppColor.Black,                      // label text when not selected
                        selectedContainerColor = AppColor.Primary,    // background when selected
                        selectedLabelColor = AppColor.White,              // label text when selected
                        iconColor = AppColor.Black,                    // icon color when not selected
                        selectedLeadingIconColor = AppColor.White         // icon color when selected
                    ),
                    leadingIcon = {
                        null
                    }
                )
            }
        }
    }
}

@Composable
fun BookingItem(item: Bookings, onOrderClick: () -> Unit) {
    var context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).build()

    val serviceImage = item.serviceDetails.serviceImages.find { it.isPrimary == true }?.croppedUrl

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .padding(vertical = 8.dp),
        onClick = { onOrderClick() },
        colors = CardColors(
            containerColor = AppColor.White,
            contentColor = AppColor.Black,
            disabledContainerColor = AppColor.White,
            disabledContentColor = AppColor.Black
        ),
        elevation = CardDefaults.cardElevation(8.dp) // ✅ proper way
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (serviceImage != null) {
                AsyncImage(
                    model = serviceImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                        .height(175.dp),
                    imageLoader = imageLoader
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.service_default),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = true)
                        .height(175.dp),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .background(color = AppColor.White)
                    .weight(2f, fill = true)
                    .padding(start = 20.dp, end = 16.dp, top = 15.dp, bottom = 15.dp)
            ) {

                Text(
                    text = item.serviceDetails.category.categoryName, style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                        color = AppColor.Black,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(color = AppColor.PrimaryBG)
                        .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp)

                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = item.serviceDetails.serviceName, style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                        color = AppColor.Black,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (item.routeInfo != null) {
                    RouteInfoComponent(item.routeInfo)
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}