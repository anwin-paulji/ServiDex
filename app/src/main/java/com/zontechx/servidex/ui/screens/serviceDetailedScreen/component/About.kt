package com.zontechx.servidex.ui.screens.serviceDetailedScreen.component

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.model.response.RouteInfo
import com.zontechx.servidex.model.response.TravelTime
import com.zontechx.servidex.ui.components.RouteInfoComponent
import com.zontechx.servidex.ui.screens.serviceDetailedScreen.vm.ServiceDetailedVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM
import com.zontechx.servidex.utils.GeoUtils.calculateDistance
import com.zontechx.servidex.utils.GeoUtils.estimateTravelTime

@Preview
@Composable
fun About(vm: ServiceDetailedVM = viewModel()) {
    val TAG = "About"
    val profileImage = "https://www.gravatar.com/avatar/2c7d99fe281ecd3bcd65ab915bac6dd5?s=250"
    val context = LocalContext.current
    val sharedVM: SharedVM = LocalSharedViewModel.current
    val imageLoader = ImageLoader.Builder(context).build()

    val serviceDetailsState by vm.serviceDetails.collectAsState()
    val serviceDescription = serviceDetailsState?.serviceDescription ?: ""
    val providerName = serviceDetailsState?.serviceProvider?.name ?: ""
    val providerMobileNumber = serviceDetailsState?.serviceProvider?.mobileNumber ?: ""

    var routeInfoData by remember { mutableStateOf<RouteInfo?>(null) }

    LaunchedEffect(Unit) {
        sharedVM.currentAddress.value?.let {
            val distance: Double = calculateDistance(
                lat1 = it.latitude,
                lon1 = it.longitude,
                lat2 = serviceDetailsState?.location?.lat ?: 0.0,
                lon2 = serviceDetailsState?.location?.long ?: 0.0
            )
            val (hours, minutes) = estimateTravelTime(distance)

            val routeData = RouteInfo(
                distance = distance.toString(),
                travelTime = TravelTime(
                    hours = hours,
                    minutes = minutes
                )
            )

            routeInfoData = routeData
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White)
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {
        // Description Section
        Text(
            text = "Description",
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                color = AppColor.Black
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = serviceDescription,
            maxLines = 3,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.roboto_regular)),
                color = AppColor.gray_4
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Provider Section
        Text(
            text = "Provider",
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                color = AppColor.Black
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(profileImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Provider profile image",
                    imageLoader = imageLoader,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .size(60.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = AppColor.yellow_1),
                    contentScale = ContentScale.Crop
                )

                Column {
                    Text(
                        text = providerName,
                        modifier = Modifier.padding(bottom = 10.dp),
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                            color = AppColor.Black
                        )
                    )
                    routeInfoData?.let { RouteInfoComponent(it) }

//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(4.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(2.dp)
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.ic_location_fill),
//                                contentDescription = null,
//                                modifier = Modifier.size(18.dp),
//                                tint = AppColor.Primary
//                            )
//                            Text(
//                                text = "0.8 KM",
//                                style = TextStyle(
//                                    fontSize = 16.sp,
//                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
//                                    color = AppColor.gray_4
//                                )
//                            )
//                        }
//
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(2.dp)
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.ic_watch_fill),
//                                contentDescription = null,
//                                modifier = Modifier.size(18.dp),
//                                tint = AppColor.Primary
//                            )
//                            Text(
//                                text = "45 Mins",
//                                style = TextStyle(
//                                    fontSize = 16.sp,
//                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
//                                    color = AppColor.gray_4
//                                )
//                            )
//                        }
//                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
//                RoundIconButton(
//                    iconRes = R.drawable.ic_message_fill,
//                    iconTint = AppColor.Primary,
//                    backgroundColor = AppColor.white_1
//                )
                RoundIconButton(
                    iconRes = R.drawable.ic_call_fill,
                    iconTint = AppColor.Primary,
                    backgroundColor = AppColor.white_1,
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = "tel:${providerMobileNumber}".toUri()
                        }
                        context.startActivity(intent)
                    }
                )
            }
        }

        // Provider Location
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Location : ",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = AppColor.gray_4,
                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                )
            )
//            Text(
//                text = "Thoraipakkam, Chennai",
//                style = TextStyle(
//                    fontSize = 16.sp,
//                    color = AppColor.Black,
//                    fontFamily = FontFamily(Font(R.font.roboto_medium))
//                ),
//            )
        }
    }
}