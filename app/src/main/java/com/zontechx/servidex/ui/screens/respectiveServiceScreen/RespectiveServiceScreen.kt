package com.zontechx.servidex.ui.screens.respectiveServiceScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.response.ServiceData
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.components.AnimatedComponent
import com.zontechx.servidex.ui.components.RoundIconButton
import com.zontechx.servidex.ui.screens.respectiveServiceScreen.component.FilterSheet
import com.zontechx.servidex.ui.screens.respectiveServiceScreen.component.SortBySheet
import com.zontechx.servidex.ui.screens.respectiveServiceScreen.vm.RespectiveServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun RespectiveServiceScreen(serviceId: String, source: String) {
    val TAG = "RespectiveServiceScreen"
    val vm: RespectiveServiceScreenVM = viewModel()
    Scaffold(
        containerColor = AppColor.White,
        topBar = {
            ToolBar()
        },
//        bottomBar = {
//            BottomBar()
//        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                start = 16.dp, end = 16.dp,
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
//            SearchBar()
            ServiceList(serviceId = serviceId)
        }
    }

    FilterSheet()
    SortBySheet()
}

@Preview
@Composable
fun ToolBar() {
    val rootNavController = LocalRootNavController.current
    val screenName = AppConstant.ScreenSource.FROM_RESPECTIVE_SERVICE

    fun onBackClick() {
        rootNavController.popBackStack()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        AnimatedComponent(
            onClick = { onBackClick() },
            content = {
                RoundIconButton(
                    backgroundColor = AppColor.White,
                    iconTint = AppColor.Black,
                    iconRes = R.drawable.arrow_left,
                )
            }
        )
    }
}

@Preview
@Composable
fun SearchBar() {

    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .padding(top = 5.dp, bottom = 5.dp),
        placeholder = { Text("Search...", style = TextStyle(fontSize = 14.sp)) },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
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
}

@Composable
fun ServiceList(serviceId: String) {
    val vm: RespectiveServiceScreenVM = viewModel()
    val serviceResponse by vm.serviceListResponse.collectAsState()

    val listState = rememberLazyListState()

    // Trigger next page when scrolled to bottom
    val shouldLoadNextPage = remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisible >= totalItems - 5 // threshold
        }
    }

    LaunchedEffect(shouldLoadNextPage.value) {
        if (shouldLoadNextPage.value) {
            vm.specificServiceList(serviceId)
        }
    }

    when (serviceResponse) {
        is ApiResponse.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }

        is ApiResponse.Success -> {
            val services = (serviceResponse as ApiResponse.Success).data
            LazyColumn(state = listState) {

                items(services) { item ->
                    ServiceItem(item)
                }

                // Show loading at bottom if more data is being fetched
                if (vm.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }

        is ApiResponse.Error -> {
            Text(
                text = (serviceResponse as ApiResponse.Error).message,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        else -> {}
    }
}


@Composable
fun ServiceItem(item: ServiceData) {
    var context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).build()

    val rootNavController = LocalRootNavController.current
    val serviceImage = item.serviceImages.find { it.isPrimary == true }?.croppedUrl

    fun onItemClick() {
        // Handle item click
        val serviceId = item._id
        rootNavController.navigate(
            Screen.ServiceDetailed.createRoute(
                serviceId = serviceId,
                source = AppConstant.ScreenSource.FROM_RESPECTIVE_SERVICE
            )
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .padding(vertical = 8.dp),
        colors = CardColors(
            containerColor = AppColor.White,
            contentColor = AppColor.Black,
            disabledContainerColor = AppColor.White,
            disabledContentColor = AppColor.Black
        ),
        onClick = { onItemClick() },
        elevation = CardDefaults.cardElevation(8.dp) // ✅ proper way
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    text = item.primaryCategory.categoryName, style = TextStyle(
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

                Text(
                    text = item.serviceDescription, style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        color = AppColor.gray_4,
                        textAlign = TextAlign.Start
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                AnimatedButton(
                    text = AppConstant.BOOK_NOW,
                    fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                    textColor = AppColor.White,
                    textSize = 14.sp,
                    backgroundColor = AppColor.Primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    textPaddingValues = PaddingValues(
                        start =
                            15.dp, end = 15.dp, bottom = 5.dp, top = 5.dp
                    ),
                    paddingStart = 0.dp,
                    paddingEnd = 0.dp,
                    onClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomBar() {
    val TAG = "BottomBar"

    fun onSortClick() {
        Log.d(TAG, "onSortClick")
    }

    fun onFilterClick() {
        Log.d(TAG, "onFilterClick")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(AppColor.White)
    ) {
        HorizontalDivider(thickness = 1.dp, color = AppColor.gray_2)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onFilterClick() }
                    .background(color = AppColor.White)
                    .padding(start = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 15.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sort_ic),
                        contentDescription = "Sort",
                        tint = AppColor.Black,
                        modifier = Modifier.size(20.dp) // keep consistent size
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // spacing between icon and text

                    Text(
                        text = AppConstant.SORT_BY,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            color = AppColor.Black
                        )
                    )
                }
            }
            VerticalDivider(
                color = AppColor.gray_2,
                thickness = 1.dp,
                modifier = Modifier.height(43.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onFilterClick() }
                    .padding(end = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 15.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter_ic),
                        contentDescription = "Filter",
                        tint = AppColor.Black,
                        modifier = Modifier.size(20.dp) // keep consistent size
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // spacing between icon and text

                    Text(
                        text = AppConstant.FILTER,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.roboto_regular)),
                            color = AppColor.Black
                        )
                    )
                }
            }

        }
    }
}