package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun SubCategoryList() {

    val TAG = "SubCategoryList"

    val vm: AddServiceScreenVM = viewModel()

    val selectedCategory by vm.selectedCategory.collectAsState()

    var subCategory = selectedCategory.subCategory

    val titleTextStyle =
        TextStyle(color = AppColor.Black, fontSize = 16.sp, fontWeight = FontWeight.W400)
    val titleModifier = Modifier.padding(vertical = 10.dp)
    val checkBoxModifier = Modifier.padding(vertical = 10.dp)

    val onClickItem: (_index: Int) -> Unit = { index ->
        vm.updateSubCategory(index = index)
    }

    LaunchedEffect(selectedCategory) {

        selectedCategory.subCategory.let {
            subCategory = it
        }
    }

    LazyColumn {
        items(subCategory.size) { index ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(true) { onClickItem(index) }) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = subCategory[index].name.toString(),
                        modifier = titleModifier.padding(start = 30.dp).weight(4f),
                        style = titleTextStyle,
                    )

                    RadioButton(
                        selected = subCategory[index].isSelected == true,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = AppColor.Yellow,
                            unselectedColor = AppColor.darkGray
                        ),
                        onClick = { onClickItem(index) },
                        modifier = checkBoxModifier.weight(1f)
                    )
                }
                Divider(color = AppColor.gray_2, thickness = 1.dp)
            }
        }
    }
}