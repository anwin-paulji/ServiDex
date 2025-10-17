package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.data.model.response.CategoryListItem
import com.zontechx.servidex.model.sealed.CategoryItemModel
import com.zontechx.servidex.ui.provider.addServiceScreen.CategoryItem
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun CategoryGrid(
    categoryList: List<CategoryListItem?>,
    vm: AddServiceScreenVM = viewModel() // ideally hoist this outside
) {
    val selectedId = vm.selectedCategory.collectAsState().value.id

    val updatedList = remember(categoryList, selectedId) {
        categoryList.filterNotNull().map {
            it.copy(isSelected = it.id == selectedId)
        }
    }

    var onClickCard: (index:Int, data:CategoryListItem) -> Unit = {
        index, data ->
        if (vm.selectedCategory.value.id != data.id) {
            vm.onCategorySelected(index)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(start = 30.dp, end = 30.dp, bottom = 30.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(updatedList.size) { index ->
            val item = updatedList[index]

            CategoryItem(
                data = item,
                onClick = { onClickCard(index, item) }
            )

//            Surface(
//                modifier = Modifier
//                    .size(95.dp)
//                    .background(AppColor.White)
//                    .clickable { onClickCard(index, item) }
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .border(
//                            width = 2.dp,
//                            color = if (item.isSelected == true) AppColor.Yellow else AppColor.White,
//                            shape = RoundedCornerShape(8.dp)
//                        ),
//                    contentAlignment = Alignment.TopStart
//                ) {
//                    LoadUrlImage(
//                        imageUrl = item.categoryUrl.orEmpty(),
//                        modifier = Modifier.size(85.dp).fillMaxSize()
//                    )
//
//                    RadioButton(
//                        selected = item.isSelected == true,
//                        colors = RadioButtonDefaults.colors(
//                            selectedColor = AppColor.Yellow,
//                            unselectedColor = AppColor.White
//                        ),
//                        onClick = { onClickCard(index, item) },
//                        modifier = Modifier.padding(5.dp)
//                    )
//                }
//            }
        }
    }
}
