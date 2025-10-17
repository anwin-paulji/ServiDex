package com.zontechx.servidex.ui.provider.UploadServiceImage.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.provider.UploadServiceImage.vm.UploadServiceImageVM
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.theme.AppColor

@Preview
@Composable
fun BottomBar(onClickButton: () -> Unit = {}) {
    val vm: UploadServiceImageVM = viewModel()
    val responseState by vm.uploadImageListResponse.collectAsState()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 10.dp)
    ) {
        if (responseState is ApiResponse.Loading) {
            CircularProgressIndicator(color = AppColor.Primary, strokeWidth = 4.dp)
        } else {
            AnimatedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = AppConstant.UPLOAD_IMAGE,
                onClick = {
                    onClickButton()
                }
            )
        }
    }
}