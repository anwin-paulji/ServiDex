package com.zontechx.servidex.ui.screens.booking.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.theme.AppColor

object ConfirmBookingSheet {
    private var showSheet by mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Register(onClickButton1: () -> Unit, onClickButton2: () -> Unit) {
        if (showSheet) {
            ModalBottomSheet(
                containerColor = AppColor.White,
                onDismissRequest = { dismiss() },
                sheetState = rememberModalBottomSheetState()
            ) {
                Column(Modifier.padding(16.dp)) {

                    Image(
                        painter = painterResource(id = R.drawable.correct_shield),
                        contentDescription = null,
                        modifier = Modifier.height(100.dp)
                    )

                    Text("Can we confirm your booking!🤩", style = MaterialTheme.typography.headlineSmall)

                    Spacer(Modifier.height(30.dp))

                    Text(
                        "Are you sure you want to confirm your booking?",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            dismiss()
                            onClickButton1()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColor.Primary,
                            contentColor = AppColor.White
                        ),
                    ) {
                        Text("Confirm Booking")
                    }

                    Button(
                        onClick = {
                            dismiss()
                            onClickButton2()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(0.dp),
                        border = BorderStroke(width = 1.dp, color = AppColor.Primary),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColor.Transparent,
                            contentColor = AppColor.Primary
                        ),
                    ) {
                        Text("Cancel Booking")
                    }
                }
            }
        }
    }

    fun show() {
        showSheet = true
    }

    fun dismiss() {
        showSheet = false
    }
}