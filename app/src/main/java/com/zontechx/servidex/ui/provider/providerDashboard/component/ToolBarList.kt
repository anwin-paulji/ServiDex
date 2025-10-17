package com.zontechx.servidex.ui.provider.providerDashboard.component

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zontechx.servidex.model.enum.BookingStatus
import com.zontechx.servidex.ui.provider.providerDashboard.LocalProviderMainVM
import com.zontechx.servidex.ui.provider.providerDashboard.vm.ProviderMainVM
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun ToolBarList() {
    val TAG = "ToolBarList"
    val vm: ProviderMainVM = LocalProviderMainVM.current
    val toolBarData = listOf("All", "Ongoing", "Completed", "Cancelled")

    var selected by remember { mutableStateOf(toolBarData.first()) }

    LaunchedEffect(Unit) {
        Log.e(TAG, "LaunchedEffect: Triggered")
        vm.loadInitial(BookingStatus.ALL)
    }

    fun onChipSelected(item: String) {
        selected = item
        when (item) {
            "All" -> vm.loadInitial(BookingStatus.ALL)
            "Ongoing" -> vm.loadInitial(BookingStatus.IN_PROGRESS)
            "Completed" -> vm.loadInitial(BookingStatus.COMPLETED)
            "Cancelled" -> vm.loadInitial(BookingStatus.CANCELLED)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(toolBarData) { item ->
                FilterChip(
                    selected = selected == item,
                    onClick = { onChipSelected(item) },
                    label = { Text(item) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = AppColor.White,              // background when not selected
                        labelColor = AppColor.Black,                      // label text when not selected
                        selectedContainerColor = AppColor.Primary,    // background when selected
                        selectedLabelColor = AppColor.White,              // label text when selected
                        iconColor = AppColor.Black,                    // icon color when not selected
                        selectedLeadingIconColor = AppColor.White         // icon color when selected
                    ),
                    leadingIcon = {
                        null
                    }
                )
            }
        }
    }
}