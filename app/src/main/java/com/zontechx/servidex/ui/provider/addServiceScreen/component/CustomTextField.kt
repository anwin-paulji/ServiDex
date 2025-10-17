package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun CustomTextField(
    title: String = "",
    hint: String = "",
    description: String = "",
    textValue: String,
    minLines: Int = 1,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit,
) {

    var textFieldModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 5.dp)
            .fillMaxWidth()

    var titleModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 2.dp)
            .fillMaxWidth()

    var descriptionModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 15.dp)
            .fillMaxWidth()

    var titleTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var hintTextStyle =
        TextStyle(color = AppColor.gray_4, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var descritptionTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 12.sp)

    var inputTextStyle =
        TextStyle(color = AppColor.Black, fontWeight = FontWeight.W600, fontSize = 18.sp)

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.White)
    ) {
        Text(text = title, modifier = titleModifier, style = titleTextStyle)

        TextField(
            value = textValue,
            onValueChange = onValueChange,
            placeholder = { Text(text = hint, style = hintTextStyle) },
            singleLine = singleLine,
            minLines = minLines,
            textStyle = inputTextStyle,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppColor.gray_2,
                unfocusedContainerColor = AppColor.gray_2,
                focusedIndicatorColor = AppColor.gray_2,
                unfocusedIndicatorColor = AppColor.gray_2,
                cursorColor = AppColor.Black
            ),
            modifier = textFieldModifier
        )

        Text(text = description, modifier = descriptionModifier, style = descritptionTextStyle)
    }
}