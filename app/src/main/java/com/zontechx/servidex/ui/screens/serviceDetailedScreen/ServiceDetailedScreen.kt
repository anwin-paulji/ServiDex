package com.zontechx.servidex.ui.screens.serviceDetailedScreen

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.response.ServiceImage
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.screens.serviceDetailedScreen.component.BottomBar
import com.zontechx.servidex.ui.screens.serviceDetailedScreen.component.DetailsBody
import com.zontechx.servidex.ui.screens.serviceDetailedScreen.component.DetailsHeadder
import com.zontechx.servidex.ui.screens.serviceDetailedScreen.component.RoundIconButton
import com.zontechx.servidex.ui.screens.serviceDetailedScreen.vm.ServiceDetailedVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.utils.AppUtils
import com.zontechx.servidex.utils.DeeplinkUtils
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json

@Composable
fun ServiceDetailedScreen(
    rootNavController: NavHostController, serviceId: String, screenSource: String
) {
    val TAG = "ServiceDetailedScreen"
    val vm: ServiceDetailedVM = viewModel()
    val serviceDetailResponse by vm.serviceDetailResponse.collectAsState()
    val serviceDetails by vm.serviceDetails.collectAsState()
    var progress by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var loadScreen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.screenSource = screenSource
        Log.e(TAG, "Screen source: ${vm.screenSource}")
        if (serviceId.isNotEmpty()) {
            vm.getServiceById(serviceId)
            vm.loadReviewInitial(serviceId)
        }
        delay(400)
        loadScreen = true
    }

    BackHandler(enabled = true) {
        Log.e(TAG, "BackHandler called")
        rootNavController.popBackStack()
    }

    LaunchedEffect(serviceDetailResponse) {
        when (val result = serviceDetailResponse) {
            null -> {}

            ApiResponse.Initial -> {
                Log.e(TAG, "Initial ****")
            }

            ApiResponse.Loading -> {
                Log.e(TAG, "Loading ****")
                progress = true
                errorMessage = null
            }

            is ApiResponse.Success -> {
                progress = false
                errorMessage = null
                val data = result.data
                Log.d(TAG, "Data received: $data")
                Log.d(TAG, "Data Location: ${data.data?.location?.long}")
            }

            is ApiResponse.Error -> {
                progress = false
                errorMessage = result.message
                Log.e(TAG, "Failed: ${result.message}")
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (!progress && serviceDetails != null) {
                BottomBar {
                    /**
                     * We have used encodeToString to convert json string to object
                     * Reason : We are passing data from one screen to another screen using navigation
                     * If we use Serialization or Parcelable we need to be more careful about the data type
                     * */
                    var serviceJsonData: String = Uri.encode(Json.encodeToString(serviceDetails));
                    rootNavController.navigate(
                        Screen.BookingScreen.createRoute(
                            serviceJsonData = serviceJsonData,
                            source = AppConstant.ScreenSource.FROM_SERVICE_DETAIL
                        )
                    )
                }
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppColor.White)
        ) {

            Header(
                contentModifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                rootNavController = rootNavController
            )
            if (loadScreen == true) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding(), // Ensure content is above system gesture bar
                    contentAlignment = Alignment.Center
                ) {
                    when (val response = serviceDetailResponse) {
                        is ApiResponse.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                                color = AppColor.Primary,
                                strokeWidth = 4.dp
                            )
                        }

                        is ApiResponse.Success -> {
                            Details(vm)
                        }

                        is ApiResponse.Error -> {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error: ${response.message}",
                                    style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                        fontSize = 16.sp,
                                        color = Color.Red
                                    )
                                )
                            }
                        }

                        ApiResponse.Initial -> {}
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    when {
                        progress -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.Center),
                                color = AppColor.Primary,
                                strokeWidth = 4.dp
                            )
                        }

                        errorMessage != null -> {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error: $errorMessage", style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                        fontSize = 16.sp,
                                        color = Color.Red
                                    )
                                )
                            }
                        }

                        serviceDetails != null -> {
                            Details(vm)
                        }

                        else -> {
                            Text(
                                text = "No service details available",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp),
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                    fontSize = 16.sp,
                                    color = AppColor.gray_4
                                )
                            )
                        }
                    }
                }
            }

        }
    }
}

/**
 * @param contentModifier: Modifier : Used to content, not for Background image
 * Background image is in Box is Service Image which will be swipable
 * */
@Composable
fun Header(
    contentModifier: Modifier = Modifier,
    rootNavController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    val vm: ServiceDetailedVM = viewModel()
    val serviceDetails by vm.serviceDetails.collectAsState()

    fun handleShareButtonClick() {
        val serviceId = vm.selectedServiceId
        val providerName = serviceDetails?.serviceProvider?.name
        val serviceName = serviceDetails?.serviceTitle

        val deepLinkData = DeeplinkUtils.shareService(providerName ?: "", serviceName ?: "", serviceId = serviceId)
        AppUtils.shareData(context, deepLinkData)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(color = AppColor.gray),
    ) {
        HorizontalImageList(imageList = serviceDetails?.serviceImages)

        Column(modifier = contentModifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RoundIconButton(
                    iconRes = R.drawable.arrow_left, onClick = {
                        rootNavController.popBackStack()
                    }
                )

                if(serviceDetails != null) {
                    Row {
                        RoundIconButton(
                            iconRes = R.drawable.share, onClick = { handleShareButtonClick() })
//                        Spacer(modifier = Modifier.width(8.dp))
//                        RoundIconButton(
//                            iconRes = R.drawable.fav, onClick = {})
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalImageList(imageList: List<ServiceImage>?) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).build()

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { imageList?.size ?: 0 }
    )

    val sortedList = imageList?.sortedByDescending { it.isPrimary }

    if(imageList.isNullOrEmpty()) {
        Image(
            painter = painterResource(R.drawable.service_default),
            contentDescription = null,
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .size(300.dp),
            contentScale = ContentScale.Crop,
        )
    } else {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            AsyncImage(
                model = sortedList?.get(page)?.croppedUrl,
                contentDescription = null,
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(300.dp),
                contentScale = ContentScale.Crop,
                imageLoader = imageLoader
            )
        }
    }
}

@Composable
fun Details(vm: ServiceDetailedVM) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AppColor.White)
    ) {
        Column {
            DetailsHeadder(viewModel = vm)
            DetailsBody()
        }
    }
}

@Preview
@Composable
fun HeaderPreview() {
    Header()
}

@Preview
@Composable
fun DetailsPreview() {
    Details(viewModel())
}
