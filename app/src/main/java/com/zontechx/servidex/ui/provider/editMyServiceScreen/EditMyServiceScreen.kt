package com.zontechx.servidex.ui.provider.editMyServiceScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.ui.components.AnimatedComponent
import com.zontechx.servidex.ui.provider.editMyServiceScreen.component.MyServiceDetailsUpdateSheet
import com.zontechx.servidex.ui.components.RoundIconButton
import com.zontechx.servidex.ui.provider.editMyServiceScreen.vm.EditMyServiceVM
import com.zontechx.servidex.ui.provider.editMyServiceScreen.vm.EditOption
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM
import kotlinx.coroutines.launch

@Composable
fun EditMyServiceScreen() {
    val TAG = "EditMyServiceScreen"
    val rootNavController = LocalRootNavController.current
    BackHandler { rootNavController.popBackStack() }

    EditMyServiceScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMyServiceScreenContent() {
    val TAG = "EditMyServiceScreenContent"
    val scope = rememberCoroutineScope()
    val vm: EditMyServiceVM = viewModel()
    val sharedVM: SharedVM = LocalSharedViewModel.current
    val SelectedMyServiceData by vm.selectedMyServiceData.collectAsState()
    val sharedVMSelectedMyServiceData by sharedVM.selectedMyServiceData.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showLocationPickerSheet by remember { mutableStateOf(false) }

    fun handleEditClick(selectedEditOption: EditOption) {
        vm.onChangeEditOption(option = selectedEditOption)
        showLocationPickerSheet = true
        scope.launch { sheetState.show() }
    }

    LaunchedEffect(Unit) { sharedVMSelectedMyServiceData?.let { vm.setSelectedServiceData(it) } }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppColor.White)
                .padding(
                    bottom = innerPadding.calculateBottomPadding(),
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
        ) {

            ToolBar()

            SelectedMyServiceData?.let {

                val priceType: String? = it.chargeBasis.let {
                    if (it.pricingType == AppConstant.PricingType.MINUTES) {
                        "Minute"
                    } else {
                        "Hour"
                    }
                }

                EditContent(
                    title = "Category", value = it.primaryCategory.categoryName, enableEdit = false
                )
                HorizontalDivider(thickness = 0.4.dp, color = AppColor.gray_4)

                EditContent(
                    title = "Sub-Category",
                    value = it.subCategories.subCategoryName,
                    enableEdit = false
                )
                HorizontalDivider(thickness = 0.4.dp, color = AppColor.gray_4)

                EditContent(
                    title = "Service Title", value = it.serviceTitle, onClickEdit = {
                        handleEditClick(selectedEditOption = EditOption.SERVICE_TITLE)
                    })
                HorizontalDivider(thickness = 0.4.dp, color = AppColor.gray_4)

                EditContent(
                    title = "Service Description", value = it.serviceDescription, onClickEdit = {
                        handleEditClick(selectedEditOption = EditOption.SERVICE_DESCRIPTION)
                    })
                HorizontalDivider(thickness = 0.4.dp, color = AppColor.gray_4)

                EditContent(
                    title = "Service Charges",
                    value = "${it.chargeBasis.price}/${priceType}",
                    enableEdit = false
                )
                HorizontalDivider(thickness = 0.4.dp, color = AppColor.gray_4)

                Text(
                    text = "Other details for update will be available soon.",
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 15.dp,
                        bottom = 15.dp
                    ),
                    style = TextStyle(
                        color = AppColor.gray_4,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.roboto_regular))
                    )
                )

//                EditContent(
//                    title = "Availability",
//                    value = it.primaryCategory.categoryName,
//                    enableEdit = false
//                )
//                HorizontalDivider(thickness = 0.4.dp, color = AppColor.gray_4)

//                EditContent(
//                    title = "Service Location",
//                    value = it.primaryCategory.categoryName,
//                    enableEdit = false
//                )
//                HorizontalDivider(thickness = 0.4.dp, color = AppColor.gray_4)
            }
        }
    }

    if (showLocationPickerSheet) {
        MyServiceDetailsUpdateSheet(
            sheetState = sheetState,
            onDismiss = { scope.launch { showLocationPickerSheet = false } })
    }
}

@Preview
@Composable
fun ToolBar() {
    val rootNavController = LocalRootNavController.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AppColor.White)
            .padding(top = 16.dp)
    ) {
        AnimatedComponent(onClick = { rootNavController.popBackStack() }) {
            RoundIconButton(
                iconRes = R.drawable.arrow_left, iconTint = AppColor.Black
            )
        }

        Text(
            text = "Service Details", style = TextStyle(
                color = AppColor.Black,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.roboto_semibold))
            )
        )
    }
}

@Preview
@Composable
fun EditContent(
    title: String = "Service Title",
    value: String = "Service Value",
    enableEdit: Boolean = true,
    onClickEdit: () -> Unit = {}
) {
    val TAG = "EditContent"

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AppColor.White)
            .padding(start = 16.dp, end = 16.dp, top = 15.dp, bottom = 15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title, style = TextStyle(
                    color = AppColor.gray_4,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_regular))
                )
            )
            Text(
                text = value, style = TextStyle(
                    color = AppColor.Black,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.roboto_medium))
                )
            )
        }
        if (enableEdit) {
            AnimatedComponent(onClick = { onClickEdit() }) {
                RoundIconButton(
                    iconRes = R.drawable.ic_edit, iconTint = AppColor.Primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditMyServiceScreenPreview() {
    EditMyServiceScreen()
}