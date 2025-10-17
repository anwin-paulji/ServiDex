package com.zontechx.servidex.ui.screens.booking.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.components.CustomTimePicker
import com.zontechx.servidex.ui.screens.booking.vm.BookingVM
import com.zontechx.servidex.ui.theme.AppColor
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Preview
@Composable
fun SelectDateAndTime() {

    val vm: BookingVM = viewModel()

    var timeSlots: List<TimeSlot> = listOf(
        TimeSlot(timeSlot = "08:00 AM"),
        TimeSlot(timeSlot = "09:00 AM"),
        TimeSlot(timeSlot = "10:00 AM"),
        TimeSlot(timeSlot = "11:00 AM"),
        TimeSlot(timeSlot = "12:00 PM"),
        TimeSlot(timeSlot = "01:00 PM"),
        TimeSlot(timeSlot = "02:00 PM"),
        TimeSlot(timeSlot = "03:00 PM"),
        TimeSlot(timeSlot = "04:00 PM"),
        TimeSlot(timeSlot = "05:00 PM"),
        TimeSlot(timeSlot = "06:00 PM"),
        TimeSlot(timeSlot = "07:00 PM"),
        TimeSlot(timeSlot = "08:00 PM"),
        TimeSlot(timeSlot = "09:00 PM"),
        TimeSlot(timeSlot = "10:00 PM")
    )

    var fromTime by remember {
        mutableStateOf(
            LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))
        )
    }
    var toTime by remember {
        mutableStateOf(
            LocalTime.now().plusHours(1).format(DateTimeFormatter.ofPattern("hh:mm a"))
        )
    }

    var currentSelectedTime by remember { mutableStateOf("Select Slot") }
    var showDialog by remember { mutableStateOf(false) }

    var titleModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 2.dp)
            .fillMaxWidth()

    var descriptionModifier =
        Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 15.dp)
            .fillMaxWidth()

    var titleTextStyle = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = AppColor.Black
    )

    var handleFromTime: (Int) -> Unit = { index ->
        // Find from timeSlot list
        if (index in timeSlots.indices) {
            fromTime = timeSlots[index].timeSlot
            vm.onTimeFromSelected(fromTime)
        }

    }

    var handleToTime: (Int) -> Unit = { index ->
        if (index in timeSlots.indices) {
            toTime = timeSlots[index].timeSlot
            vm.onTimeToSelected(toTime)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(color = AppColor.white_2)
    ) {

        Text(
            text = "Select Date and Time",
            modifier = titleModifier,
            style = titleTextStyle
        )

        CalendarDialogButton()

        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomTimePicker(
                title = "Start Time",
                time = fromTime,
                onClick = { showDialog = true })

            CustomTimePicker(
                title = "End Time",
                time = toTime,
                onClick = { showDialog = true })
        }

        if (showDialog) {
            TimeSlotPickerDialog(
                timeSlots = timeSlots,
                onTimeFromSelected = { it ->
                    handleFromTime(it)
                },
                onTimeToSelected = { it ->
                    handleToTime(it)
                },
                onDismissRequest = { showDialog = false }
            )
        }
    }
}


@Composable
fun TimeSlotPickerDialog(
    timeSlots: List<TimeSlot>,
    onTimeFromSelected: (Int) -> Unit,
    onTimeToSelected: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {

    val context = LocalContext.current

    var finalTimeSlot by remember { mutableStateOf<List<TimeSlot>>(timeSlots) }
    val interactionSource = remember { MutableInteractionSource() }

    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)

    var startIndex by remember { mutableStateOf<Int>(-1) }
    var endIndex by remember { mutableStateOf<Int>(-1) }


    val handleOnClickItem: (Int) -> Unit = { clickedIndex ->

        when {
            startIndex == -1 -> { // First click → select start
                startIndex = clickedIndex
                onTimeFromSelected(clickedIndex)
                endIndex = -1
                finalTimeSlot = finalTimeSlot.mapIndexed { i, t ->
                    t.copy(
                        isSelected = i == clickedIndex,
                        isDisabled = false
                    )
                }
            }

            endIndex == -1 && clickedIndex > startIndex -> { // Second click → select end
                endIndex = clickedIndex
                onTimeToSelected(clickedIndex)
                finalTimeSlot = finalTimeSlot.mapIndexed { i, t ->
                    t.copy(
                        isSelected = i == startIndex || i >= startIndex && i <= clickedIndex,
                        isDisabled = i < startIndex || i > clickedIndex
                    )
                }
            }

            else -> { // Third click → reset and select new start
                startIndex = clickedIndex
                endIndex = -1
                onTimeFromSelected(clickedIndex)
                onTimeToSelected(-1)
                finalTimeSlot = finalTimeSlot.mapIndexed { i, t ->
                    t.copy(
                        isSelected = i == clickedIndex,
                        isDisabled = false
                    )
                }
            }
        }
    }


    AlertDialog(
        shape = RoundedCornerShape(30.dp),
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                "Select Time Slot", style = TextStyle(
                    color = AppColor.gray_4,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(R.font.montserrat_bold)
                    )
                )
            )
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(finalTimeSlot.size) { index ->
                    val item = finalTimeSlot[index]
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = if (item.isDisabled) Color.Gray else Color.White)
                            .border(
                                width = if (item.isSelected) 1.dp else 0.1.dp,
                                color = if (item.isSelected) AppColor.Primary else Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource
                            ) { handleOnClickItem(index) }
                            .padding(8.dp)

                    ) {
                        Text(text = timeSlots[index].timeSlot)
                    }

                }
            }
        },
        confirmButton = {
            AnimatedButton(
                text = "Ok",
                onClick = onDismissRequest,
                textColor = AppColor.White,
                backgroundColor = AppColor.Primary,
                modifier = Modifier.padding(bottom = 16.dp),
                paddingStart = 20.dp,
                paddingEnd = 20.dp,
                paddingBottom = 5.dp,
                cornerRadius = 8.dp,
                textPaddingValues = PaddingValues(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 20.dp,
                    end = 20.dp
                )
            )
        },
        dismissButton = {
            AnimatedButton(
                text = "Close",
                textColor = AppColor.Primary,
                backgroundColor = AppColor.White,
                buttonBorderColor = AppColor.Primary,
                onClick = onDismissRequest,
                paddingStart = 10.dp,
                paddingEnd = 10.dp,
                paddingBottom = 5.dp,
                cornerRadius = 8.dp,
                textPaddingValues = PaddingValues(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 20.dp,
                    end = 20.dp
                )
            )
        }
    )
}

@Preview
@Composable
fun TimePickerDialogPreview() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(0.1.dp, AppColor.Primary, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable { }
    ) {
        Text(text = "8:00 AM")
    }
}

data class TimeSlot(
    val isDisabled: Boolean = false,
    val isSelected: Boolean = false,
    val timeSlot: String
)