package com.zontechx.servidex.ui.screens.mainScreen.page.homeScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.model.response.CategoryListItem
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.model.sealed.CategoryItemModel
import com.zontechx.servidex.ui.screens.mainScreen.component.CategoryItem
import com.zontechx.servidex.ui.screens.mainScreen.vm.MainScreenVM
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun TopCategory(viewModel: MainScreenVM = viewModel(), onClickViewAll: () -> Unit) {

    val context = LocalContext.current
    val rootNavController = LocalRootNavController.current
    val categoryResponse by viewModel.categoryList.collectAsState()

    var showTopService = remember { mutableStateOf(false) }

    val screenName = AppConstant.ScreenSource.FROM_MAIN_HOME


    LaunchedEffect(Unit) {  viewModel.getCategoryList() }

    Box(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppColor.White)
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Top Category",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W700)
                )
                Text(
                    text = "View all",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = AppColor.darkGray
                    ),
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .clickable {
                            onClickViewAll()
                        }
                )
            }

            when (val responseData = categoryResponse) {
                is ApiResponse.Loading -> {
                    Text(
                        text = "Loading...",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(fontSize = 14.sp, color = AppColor.darkGray)
                    )
                }

                is ApiResponse.Success -> {
                    if (responseData.data.isNotEmpty()) {
                        CategoryHorizontalList(responseData.data.take(4)) { categoryItem ->
                            rootNavController.navigate(
                                Screen.RespectiveServiceScreen.createRoute(
                                    serviceId = categoryItem.id, source = screenName
                                )
                            )
                        }
                    } else {
                        Text(
                            text = "No categories available",
                            modifier = Modifier.padding(16.dp),
                            style = TextStyle(fontSize = 14.sp, color = AppColor.darkGray)
                        )
                    }
                }

                is ApiResponse.Error -> {
                    Text(
                        text = "Failed to load categories",
                        modifier = Modifier.padding(16.dp),
                        style = TextStyle(fontSize = 14.sp, color = AppColor.darkGray)
                    )
                }

                else -> {}
            }

        }
    }
}

@Composable
fun CategoryHorizontalList(
    responseData: List<CategoryListItem?>,
    onClick: (CategoryListItem) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        responseData.take(4).forEachIndexed { index, category ->
            CategoryItem(
                CategoryItemModel(
                    name = category?.categoryName.toString(),
                    image = category?.categoryUrl.toString()
                ),
                modifier = Modifier.weight(1f),
                onClick = {
                    if (category != null) {
                        onClick(category)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun TopServicePreview() {
    TopCategory {}
}
