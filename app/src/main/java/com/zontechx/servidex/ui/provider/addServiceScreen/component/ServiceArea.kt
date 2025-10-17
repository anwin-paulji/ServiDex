package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.data.static.AppConstant.SELECTED_LOCATION
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor



@Composable
fun ServiceArea(
    title: String = "",
    description: String = ""
) {

    val vm: AddServiceScreenVM = viewModel()
    var serviceLocation = vm.serviceLocation.collectAsState()

    var titleModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 2.dp)
            .fillMaxWidth()

    var titleTextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = AppColor.Black
    )

    var descriptionModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 15.dp)
            .fillMaxWidth()

    var descritptionTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 12.sp)

    var isLocationOptionSelected = remember { mutableIntStateOf(0) }

    var onValueChange: (Int) -> Unit = { data ->
        isLocationOptionSelected.value = data
    }

    Column(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = AppColor.white_2)
    ) {
        Text(text = title, modifier = titleModifier, style = titleTextStyle)

        Column {

            if (serviceLocation.value != null) {
                Box(
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 15.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(AppColor.gray_2)
                ) {
                    Column(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                    ) {
                        Text(
                            text = SELECTED_LOCATION,
                            style = TextStyle(
                                color = AppColor.gray_3,
                                fontWeight = FontWeight.W500,
                                fontSize = 14.sp
                            )
                        )

                        Text(
                            text = serviceLocation.value!!.locationName.toString(),
                            modifier = Modifier.padding(top = 5.dp),
                            style = TextStyle(
                                color = AppColor.Black,
                                fontWeight = FontWeight.W400,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                RadioButton(
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppColor.Yellow,
                        unselectedColor = AppColor.darkGray
                    ),
                    onClick = {
                        onValueChange(1)
                    }, selected = if (isLocationOptionSelected.value == 1) true else false
                )

                Text(
                    text = AppConstant.USE_CHOOSE_LOCATION,
                    modifier = Modifier.clickable {
                        onValueChange(1)
                    })
            }

            AnimatedButton(
                text = AppConstant.ADD_NEW_LOCATION,
                textColor = AppColor.Primary,
                onClick = {
                    onValueChange(2)
                    vm.showGoogleMapBottomSheet(true)
                },
                modifier = Modifier.fillMaxWidth(), backgroundColor = AppColor.White,
            )
        }

        Text(text = description, modifier = descriptionModifier, style = descritptionTextStyle)
    }
}