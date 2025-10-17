package com.zontechx.servidex.ui.provider.providerDashboard.page.myService.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.response.ServiceData
import com.zontechx.servidex.ui.components.AnimatedComponent
import com.zontechx.servidex.ui.provider.providerDashboard.page.myService.vm.MyServiceVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM

@Preview
@Composable
fun MyServiceList() {

    val listState = rememberLazyListState()
    val vm: MyServiceVM = viewModel()
    val sharedVM: SharedVM = LocalSharedViewModel.current
    val rootNavController = LocalRootNavController.current
    val myServoceList by vm.myServiceList.collectAsState()
    val hasMore by vm.hasMore
    val isLoadingMore by vm.isLoadingMore

    fun handleItemClick(item: ServiceData) {
        sharedVM.setSelectedServiceData(item)
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White)
    ) {
        items(myServoceList, key = { it._id }) { myService ->
            MyServiceItem(myService) {
                handleItemClick(myService)
                rootNavController.navigate(Screen.EditMyServiceScreen.route)
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
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }.collect { lastVisibleIndex ->
            if (lastVisibleIndex == myServoceList.lastIndex && hasMore && !isLoadingMore) {
//                vm.loadMore()
            }
        }
    }
}

@Composable
fun MyServiceItem(item: ServiceData, onClickOrder: () -> Unit) {
    val TAG = "MyServiceItem"
    var context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).build()

    val serviceImage = item.serviceImages.find { it.isPrimary == true }?.croppedUrl

    val priceType: String? = item.chargeBasis.let {
        if (it.pricingType == AppConstant.PricingType.MINUTES) {
            "Minute"
        } else {
            "Hour"
        }
    }

    AnimatedComponent(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .padding(vertical = 4.dp),
        onClick = { onClickOrder() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .padding(vertical = 4.dp),
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            color = AppColor.White
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // 👇 Image
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
                        .weight(1.3f, fill = true)
                        .clip(shape = RoundedCornerShape(10.dp))
                ) {
                    if (serviceImage != null) {
                        AsyncImage(
                            model = serviceImage,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
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
                                .height(175.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .background(color = AppColor.White)
                        .weight(2f, fill = true)
                        .padding(start = 20.dp, end = 16.dp, top = 15.dp, bottom = 15.dp)
                ) {

                    Text(
                        text = item.primaryCategory.categoryName,
                        style = TextStyle(
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
                        text = item.serviceTitle, style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                            color = AppColor.Black,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

//                    if (item.routeInfo != null) {
//                        RouteInfo(item.routeInfo)
//                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (item.chargeBasis != null) {
                        Row(
                            modifier = Modifier
                                .navigationBarsPadding()
                                .background(color = AppColor.White)
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "₹${item.chargeBasis.price}", style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                                        fontSize = 16.sp,
                                        color = AppColor.Black
                                    )
                                )
                                Text(
                                    text = " /${priceType}", style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                                        fontSize = 12.sp,
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
}