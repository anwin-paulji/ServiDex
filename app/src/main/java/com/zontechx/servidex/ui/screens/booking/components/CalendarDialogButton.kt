package com.zontechx.servidex.ui.screens.booking.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.screens.booking.vm.BookingVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.utils.TimeUtils
import java.util.Calendar
import java.util.concurrent.TimeUnit

@Preview
@Composable
fun CalendarDialogButton(
    title: String = "Select Time"
) {
    val vm: BookingVM = viewModel()
    var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDate) {
        vm.onDateSelected(TimeUtils.formatMillisToDate(selectedDate))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(0.1.dp, AppColor.gray_4, RoundedCornerShape(8.dp))
            .clickable { showDatePicker = true },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.calender_80),
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(AppColor.gray_4),
                contentDescription = "Calendar icon"
            )

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = TextStyle(
                        color = AppColor.Primary,
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp
                    )
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = TimeUtils.formatMillisToDate(selectedDate),
                    style = TextStyle(
                        color = AppColor.gray_4,
                        fontWeight = FontWeight.W400,
                        fontSize = 18.sp
                    )
                )
            }

            Image(
                painter = painterResource(R.drawable.arrow_down),
                modifier = Modifier.size(18.dp),
                colorFilter = ColorFilter.tint(AppColor.gray_4),
                contentDescription = "Dropdown arrow"
            )
        }
    }

    if (showDatePicker) {
        CustomDatePicker(
            selectedDateMillis = selectedDate,
            onDateSelected = { newDate ->
                selectedDate = newDate
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    selectedDateMillis: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit = {}
) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val today = calendar.timeInMillis
    val maxDate = today + TimeUnit.DAYS.toMillis(20)

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis,
        initialDisplayedMonthMillis = selectedDateMillis,
        yearRange = 2025..2026,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis in today..maxDate
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis ?: today)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = Color(0xFF1976D2),
                selectedDayContentColor = Color.White
            )
        )
    }
}
