package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.data.static.AppConstant.SELECT_CATEGORY
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.provider.addServiceScreen.component.CategoryListSheet.dismiss
import com.zontechx.servidex.ui.provider.addServiceScreen.component.CategoryListSheet.titleModifier
import com.zontechx.servidex.ui.provider.addServiceScreen.component.CategoryListSheet.titleTextStyle
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor

object CategoryListSheet {

    private var showSheet by mutableStateOf(false)

    var titleTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W500, fontSize = 18.sp)

    var titleModifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 10.dp)


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Register(vm: AddServiceScreenVM = viewModel(), onApply: () -> Unit) {

        val selectedId = vm.selectedCategory.collectAsState().value.id

        var sheet = rememberModalBottomSheetState()

        if (showSheet) {
            CustomBottomSheetLayout(
                header = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = SELECT_CATEGORY,
                            style = titleTextStyle,
                            modifier = titleModifier
                        )

                        if (selectedId != null) {
                            AnimatedButton(
                                text = AppConstant.APPLY,
                                onClick = {
                                    onApply()
                                    dismiss()
                                },
                                textPaddingValues = PaddingValues(
                                    start = 20.dp,
                                    end = 20.dp,
                                    bottom = 8.dp,
                                    top = 8.dp
                                )
                            )
                        }
                    }
                },
                content = {
                    CategoryList()
                },
                onDismiss = {
                    onApply()
                    dismiss()
                },
                expandPercentage = 0.9f,
                sheetRef = sheet
            )
        }
    }

    fun show() {
        showSheet = true
    }

    fun dismiss() {
        showSheet = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheetLayout(
    header: @Composable () -> Unit = { BottomSheetDefaults.DragHandle() },
    content: @Composable () -> Unit,
    onDismiss: () -> Unit,
    expandPercentage: Float = 1f,
    draggableOnContent: Boolean = true,
    sheetRef: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { draggableOnContent })
) {

    ModalBottomSheet(
        containerColor = AppColor.White,
        onDismissRequest = { onDismiss() },
        sheetState = sheetRef,
        contentColor = Color.White,
    ) {
        Box(
            modifier = Modifier
                .background(color = AppColor.White)
                .fillMaxWidth()
                .wrapContentHeight()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * expandPercentage)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {

                header()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 200.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CategoryListSheet_Preview() {
    ModalBottomSheet(
        contentColor = AppColor.White,
        onDismissRequest = { dismiss() },
        sheetState = rememberModalBottomSheetState()
    ) {

        CustomBottomSheetLayout(
            header = {
                Text(text = SELECT_CATEGORY, style = titleTextStyle, modifier = titleModifier)
            },
            content = {
                CategoryList()
            },
            onDismiss = {
                dismiss()
            }
        )
    }
}