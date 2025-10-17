package com.zontechx.servidex.ui.provider.editMyServiceScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.provider.addServiceScreen.component.CustomTextField
import com.zontechx.servidex.ui.provider.editMyServiceScreen.vm.EditMyServiceVM
import com.zontechx.servidex.ui.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyServiceDetailsUpdateSheet(sheetState: SheetState, onDismiss: () -> Unit) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val vm: EditMyServiceVM = viewModel()

    LaunchedEffect(Unit) {
        vm.updateServiceResponse.collect {
            when (it) {
                is ApiResponse.Success -> {
                    sheetState.hide()
                    onDismiss()
                }

                is ApiResponse.Error -> {
                    sheetState.hide()
                    onDismiss()
                }

                else -> {}
            }
        }
    }

    ModalBottomSheet(
        containerColor = AppColor.White,
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        contentColor = Color.Black,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
        ),
    ) {
        MyServiceDetailsUpdateContent()
    }
}


@Preview
@Composable
fun MyServiceDetailsUpdateContent() {
    val context = LocalContext.current
    val vm: EditMyServiceVM = viewModel()
    var progress by remember { mutableStateOf<Boolean>(false) }
    val editOptionTitle by vm.editOptionTitle.collectAsState()
    val editOptionValue by vm.editOptionValue.collectAsState()
    val editOptionDescription by vm.editOptionDescription.collectAsState()

    LaunchedEffect(Unit) {
        vm.updateServiceResponse.collect {
            when (it) {
                is ApiResponse.Success -> {
                    progress = false
                }

                is ApiResponse.Error -> {
                    progress = false
                }

                is ApiResponse.Loading -> {
                    progress = true
                }

                else -> {}
            }
        }
    }

    Box(
        modifier = Modifier
            .background(color = AppColor.White)
            .fillMaxWidth()
            .wrapContentHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Edit Service Details",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp, top = 5.dp, bottom = 5.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.Black,
                    textAlign = TextAlign.Start,
                    letterSpacing = 0.5.sp
                )
            )

            CustomTextField(
                title = editOptionTitle,
                hint = "",
                description = editOptionDescription,
                textValue = editOptionValue,
                onValueChange = { vm.onChangeEditValue(it) })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            ) {

                if (progress) {
                    CircularProgressIndicator(
                        trackColor = AppColor.Primary,
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center)
                    )
                } else {
                    AnimatedButton(
                        text = "Submit",
                        backgroundColor = if (editOptionValue.isNotEmpty()) AppColor.Primary else AppColor.gray_5,
                        onClick = {
                            if (editOptionValue.isNotEmpty()) {
                                vm.onSaveEditOption()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                }
            }
        }
    }
}