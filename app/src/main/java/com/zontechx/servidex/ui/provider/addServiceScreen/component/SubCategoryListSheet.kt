package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.data.static.AppConstant.SELECT_SUB_CATEGORY
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.provider.addServiceScreen.component.CategoryListSheet.titleModifier
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor

object SubCategoryListSheet {

    private var showSheet by mutableStateOf(false)

    var titleTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 18.sp)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Register(vm: AddServiceScreenVM = viewModel(), onApply: () -> Unit) {

        val subCategories = vm.selectedCategory.collectAsState().value
        val isAnySubCategorySelected = subCategories.subCategory.any { it.isSelected }

        if (showSheet) {
            CustomBottomSheetLayout(
                header = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = SELECT_SUB_CATEGORY, style = titleTextStyle, modifier = titleModifier)


                        if(isAnySubCategorySelected) {
                            AnimatedButton(
                                text = AppConstant.APPLY,
                                onClick = {
                                    onApply()
                                    dismiss()
                                },
                                textPaddingValues = PaddingValues(start = 20.dp, end = 20.dp, bottom = 8.dp, top = 8.dp)
                            )
                        }
                    }
                },
                content = {
                    SubCategoryList()
                },
                expandPercentage = 0.9f,
                onDismiss = {
                    onApply()
                    dismiss()
                }
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