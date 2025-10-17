package com.zontechx.servidex.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zontechx.servidex.ui.theme.AppColor
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SelectDatePicker(
    title: String = "",
    onDateValidChange: (Boolean) -> Unit = {},
    onDateChangeValue: (String) -> Unit = {}
) {
    var titleTextStyle =
        TextStyle(color = AppColor.gray_3, fontWeight = FontWeight.W400, fontSize = 16.sp)

    var dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    var enableButton = remember { mutableStateOf(false) }

    val birthDate: LocalDate? = dateState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    var isDateValie = remember(birthDate) {
        derivedStateOf {
            birthDate?.let { date ->
                val today = LocalDate.now()
                val eighteenYearAgo = today.minusYears(18)
                date.isBefore(eighteenYearAgo) || date.isEqual(eighteenYearAgo)
            } == true
        }
    }

    LaunchedEffect(isDateValie) {
        val backendFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val uiFormat = DateTimeFormatter.ofPattern("dd MMM yyyy")

        onDateValidChange(isDateValie.value)
        onDateChangeValue(birthDate?.format(backendFormat) ?: "")
        enableButton.value = isDateValie.value
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = Color.White)
    ) {

        DatePicker(
            title = {
                Text(
                    text = title,
                    style = titleTextStyle,
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp)
                )
            },
            state = dateState,
            showModeToggle = true,
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                titleContentColor = AppColor.Yellow,
                headlineContentColor = AppColor.Black,
                selectedYearContainerColor = AppColor.Yellow,
                todayDateBorderColor = AppColor.Yellow,
                todayContentColor = AppColor.Yellow,
                selectedDayContainerColor = AppColor.Yellow
            )
        )
    }
}