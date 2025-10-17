package com.zontechx.servidex.ui.provider.addServiceScreen.component

import android.widget.Toast
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun CategoryList() {

    var context = LocalContext.current
    var vm: AddServiceScreenVM = viewModel()
    val _categoryList by vm.categoryList.collectAsState()

    when (val categoryList = _categoryList) {
        is ApiResponse.Initial -> {}
        is ApiResponse.Success -> {
            CategoryGrid(categoryList.data)
        }

        is ApiResponse.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .width(30.dp)
                    .height(30.dp),
                color = AppColor.Primary,
                strokeWidth = 1.dp,
                strokeCap = StrokeCap.Round
            )
        }

        is ApiResponse.Error -> {
            Toast.makeText(context, categoryList.message, Toast.LENGTH_SHORT).show()
        }
    }
}