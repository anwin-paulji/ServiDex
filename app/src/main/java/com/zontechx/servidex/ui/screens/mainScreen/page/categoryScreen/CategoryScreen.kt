package com.zontechx.servidex.ui.screens.mainScreen.page.categoryScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.data.model.response.CategoryListItem
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.model.sealed.CategoryItemModel
import com.zontechx.servidex.ui.screens.mainScreen.LocalMainVM
import com.zontechx.servidex.ui.screens.mainScreen.component.CategoryItem
import com.zontechx.servidex.ui.screens.mainScreen.vm.MainScreenVM
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun CategoryScreen() {
    val TAG = "CategoryScreen"

    val vm: MainScreenVM = LocalMainVM.current
    val categoryResponse by vm.categoryList.collectAsState()
    val dashboardDataResponse by vm.dashboardData.collectAsState()

    LaunchedEffect(categoryResponse) {
        if (categoryResponse is ApiResponse.Initial) {
            Log.d(TAG, "Calling API to get category list")
            when (val responseData = dashboardDataResponse) {
                is ApiResponse.Success -> {
                    Log.e(TAG, "Dashboard data: ${responseData.data}")
                }

                is ApiResponse.Error -> {
                    Log.d(TAG, "Dashboard data is initial")
                }

                is ApiResponse.Initial -> {
                    Log.d(TAG, "Dashboard data is initial")
                }

                else -> {}
            }
            vm.getCategoryList()
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.White)
                .padding(
                    top = innerPadding.calculateTopPadding(), start = 15.dp, end = 15.dp
                )
        ) {
            Text(
                text = "Category Screen",
                fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                fontSize = 20.sp,
                color = AppColor.Black,
                modifier = Modifier.padding(
                    start = 8.dp, end = 8.dp, top = 15.dp, bottom = 15.dp
                )
            )

            when (val response = categoryResponse) {
                is ApiResponse.Success<*> -> {
                    val categories = response.data as? List<CategoryListItem?>

                    if (categories.isNullOrEmpty()) {
                        Text("No categories found")
                        return@Column
                    }
                    CategoryList(categories)
                }

                is ApiResponse.Loading -> {
                    Text("Loading...")
                }

                is ApiResponse.Error -> {
                    Text("Failed to load categories")
                    Log.e(TAG, "Error: ${(categoryResponse as ApiResponse.Error).message}")
                }

                ApiResponse.Initial -> {
                    // Show nothing yet
                }
            }
        }
    }
}


@Composable
fun CategoryList(categories: List<CategoryListItem?>?) {
    val rootNavController = LocalRootNavController.current
    val screenName = AppConstant.ScreenSource.FROM_MAIN_CATEGORY

    fun handleOnClick(categoryId: String) {
        rootNavController.navigate(
            Screen.RespectiveServiceScreen.createRoute(
                serviceId = categoryId, source = screenName
            )
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories!!.size) { index ->
            CategoryItem(
                data = CategoryItemModel(
                    name = categories.get(index)?.categoryName ?: "Unknown",
                    image = categories.get(index)?.categoryUrl ?: "Unknown",
                ), modifier = Modifier.padding(8.dp), onClick = {
                    handleOnClick(categories[index]!!.id)
                })
        }
    }
}