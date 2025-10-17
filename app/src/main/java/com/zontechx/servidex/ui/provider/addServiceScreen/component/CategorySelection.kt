package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant.SELECT_CATEGORY
import com.zontechx.servidex.data.static.AppConstant.SELECT_SUB_CATEGORY
import com.zontechx.servidex.ui.theme.AppColor
import androidx.compose.runtime.*
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM

@Composable
fun CategorySelection(
    onClickCategory: () -> Unit,
    onClickSubCategory: () -> Unit,
    vm: AddServiceScreenVM = viewModel()
) {
    // Constants (won't trigger recomposition)
    val TAG = "CategorySelection"
    val categoryHint = "Select Category"

    // Remembered modifiers (stable across recompositions)
    val titleModifier = remember {
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 2.dp)
            .fillMaxWidth()
    }

    val textFieldModifier = remember {
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 5.dp)
            .fillMaxWidth()
    }

    val descriptionModifier = remember {
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 15.dp)
            .fillMaxWidth()
    }

    // Stable text styles (could also be moved to Theme)
    val titleTextStyle = remember {
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 16.sp)
    }

    val hintTextStyle = remember {
        TextStyle(color = AppColor.gray_4, fontWeight = FontWeight.W400, fontSize = 16.sp)
    }

    val descritptionTextStyle = remember {
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 12.sp)
    }

    val inputTextStyle = remember {
        TextStyle(color = AppColor.Black, fontWeight = FontWeight.W600, fontSize = 18.sp)
    }

    val selectedCategory by vm.selectedCategory.collectAsState()

    // Derived values
    val subCategoryName = remember(selectedCategory) {
        selectedCategory.subCategory.find { it.isSelected == true }?.name.orEmpty()
    }

    val categoryDisplayName = remember(selectedCategory) {
        selectedCategory.categoryName ?: ""
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.White)
    ) {
        Text(text = SELECT_CATEGORY, modifier = titleModifier, style = titleTextStyle)

        SelectionTextField(
            value = categoryDisplayName,
            hint = categoryHint,
            onClick = onClickCategory,
            modifier = textFieldModifier,
            hintStyle = hintTextStyle,
            inputStyle = inputTextStyle
        )

        SelectionTextField(
            value = subCategoryName,
            hint = SELECT_SUB_CATEGORY,
            onClick = onClickSubCategory,
            modifier = textFieldModifier,
            hintStyle = hintTextStyle,
            inputStyle = inputTextStyle
        )

        Text(
            text = SELECT_CATEGORY,
            modifier = descriptionModifier,
            style = descritptionTextStyle
        )
    }
}

@Composable
private fun SelectionTextField(
    value: String,
    hint: String,
    onClick: () -> Unit,
    modifier: Modifier,
    hintStyle: TextStyle,
    inputStyle: TextStyle
) {
    TextField(
        value = value,
        onValueChange = { },
        enabled = false,
        placeholder = { Text(text = hint, style = hintStyle) },
        singleLine = true,
        textStyle = inputStyle,
        suffix = {
            Image(
                painter = painterResource(R.drawable.arrow_down),
                contentDescription = null,
                modifier = Modifier.padding(end = 10.dp)
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = AppColor.gray_2,
            unfocusedContainerColor = AppColor.gray_2,
            focusedIndicatorColor = AppColor.gray_2,
            unfocusedIndicatorColor = AppColor.gray_2,
            disabledContainerColor = AppColor.gray_2,
            cursorColor = AppColor.Black
        ),
        modifier = modifier.clickable(onClick = onClick)
    )
}
