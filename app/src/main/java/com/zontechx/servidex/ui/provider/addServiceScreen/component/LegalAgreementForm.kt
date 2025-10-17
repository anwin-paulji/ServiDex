package com.zontechx.servidex.ui.provider.addServiceScreen.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM

@Composable
fun LegalAgreementForm(vm: AddServiceScreenVM = viewModel()) {

    val TAG = "LegalAgreementForm"

    LaunchedEffect(Unit) {
        Log.e(TAG, "Launched : ServiceImage")
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {}
}