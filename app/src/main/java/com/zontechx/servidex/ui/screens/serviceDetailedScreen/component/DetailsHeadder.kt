package com.zontechx.servidex.ui.screens.serviceDetailedScreen.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.screens.serviceDetailedScreen.vm.ServiceDetailedVM
import com.zontechx.servidex.ui.theme.AppColor

@Preview
@Composable
fun DetailsHeadder(viewModel: ServiceDetailedVM = viewModel()) {
    val TAG = "DetailsHeadder"
    val serviceDetailsState by viewModel.serviceDetails.collectAsState()

    val categoryName =
        if (viewModel.screenSource == AppConstant.ScreenSource.FROM_MAIN_HOME) {
            serviceDetailsState?.primaryCategory?.categoryName?.replace("Services", "", ignoreCase = true)?.trim()
        } else {
            serviceDetailsState?.categories?.firstOrNull()?.categoryName?.replace("Services", "", ignoreCase = true)?.trim()
        }

    val subCategoryName = serviceDetailsState?.subCategories?.subCategoryName ?: ""

    // React to state changes and update UI directly from state
    LaunchedEffect(serviceDetailsState) {
        Log.e(TAG, "serviceDetailsState: $serviceDetailsState")
    }

    val robotoMediumFont = FontFamily(Font(R.font.roboto_medium, FontWeight.Normal))

    val robotoRegularFont = FontFamily(Font(R.font.roboto_regular, FontWeight.Normal))

    val shape = RoundedCornerShape(50.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.White)
            .padding(start = 15.dp, end = 15.dp, top = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = categoryName?:"", // Use default or get from category API
                style = TextStyle(
                    fontSize = 16.sp,
                    color = AppColor.Primary,
                    fontFamily = robotoMediumFont
                ),
                modifier = Modifier
                    .clip(shape)
                    .background(AppColor.PrimaryBG)
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
            )
            Text(
                text = "⭐ 4.9 (365 Reviews)", // Use default or get from reviews API
                style = TextStyle(
                    fontSize = 16.sp,
                    color = AppColor.gray_4,
                    fontFamily = robotoRegularFont
                ),
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 6.dp, start = 12.dp, end = 12.dp)
            )
        }
        Text(
            text = subCategoryName, // Use default or get from subcategory API
            style = TextStyle(
                fontSize = 24.sp,
                color = AppColor.Black,
                fontFamily = robotoMediumFont
            ),
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Text(
            text = serviceDetailsState?.serviceTitle ?: "",
            style = TextStyle(
                fontSize = 16.sp,
                color = AppColor.gray_4,
                fontFamily = robotoRegularFont
            )
        )
    }
}