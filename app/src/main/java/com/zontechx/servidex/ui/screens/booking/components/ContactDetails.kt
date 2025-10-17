package com.zontechx.servidex.ui.screens.booking.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.data.static.AppConstant.CONTACT_DETAILS
import com.zontechx.servidex.data.static.AppConstant.CONTACT_PERSON_NAME
import com.zontechx.servidex.data.static.AppConstant.CONTACT_PERSON_NAME_HINT
import com.zontechx.servidex.ui.screens.booking.vm.BookingVM
import com.zontechx.servidex.ui.theme.AppColor
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant.CONTACT_PERSON_NUMBER_HINT

@Preview
@Composable
fun ContactDetails(vm: BookingVM = viewModel()) {

    val contactPersonName by vm.contactPersonName.collectAsState()
    val contactPersonNumber by vm.contactPersonNumber.collectAsState()
    var openView by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = AppColor.white_2)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { openView = !openView }
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = CONTACT_DETAILS,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppColor.Black
                ),
                modifier = Modifier.padding(start = 15.dp, end = 15.dp)
            )
            Image(
                painter = painterResource(R.drawable.arrow_down),
                contentDescription = null,
                modifier = Modifier.padding(end = 10.dp).rotate(degrees = if(openView) 180f else 0f)
            )
        }

        AnimatedVisibility(visible = openView) {
            CustomTextField(
                title = CONTACT_PERSON_NAME,
                hint = CONTACT_PERSON_NAME_HINT,
                description = CONTACT_PERSON_NUMBER_HINT,
                minLines = 1,
                singleLine = true,
                textValue = contactPersonName,
                onValueChange = { vm.onContactPersonNameChange(it) }
            )
        }
    }
}

@Composable
fun CustomTextField(
    title: String = "",
    hint: String = "",
    description: String = "Description",
    textValue: String,
    minLines: Int = 1,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit,
) {

    var textFieldModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()

    var titleModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 2.dp)
            .fillMaxWidth()

    var descriptionModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxWidth()

    var titleTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var hintTextStyle =
        TextStyle(color = AppColor.gray_4, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var descritptionTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 12.sp)

    var inputTextStyle =
        TextStyle(color = AppColor.Black, fontWeight = FontWeight.W600, fontSize = 18.sp)

    Column {
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
