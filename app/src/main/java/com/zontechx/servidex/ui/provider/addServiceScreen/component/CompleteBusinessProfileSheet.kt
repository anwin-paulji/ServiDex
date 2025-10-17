package com.zontechx.servidex.ui.provider.addServiceScreen.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.data.static.AppConstant.FULL_NAME
import com.zontechx.servidex.data.static.AppConstant.FULL_NAME_DESCRIPTION
import com.zontechx.servidex.data.static.AppConstant.FULL_NAME_HINT
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteBusinessProfileSheet() {

    val vm: AddServiceScreenVM = viewModel()

    val showProfileComeletionSheet by vm.showProfileComeletionSheet.collectAsState()

    var expandPercentage = 0.9f

    var sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true })

    LaunchedEffect(key1 = showProfileComeletionSheet) {
        if (showProfileComeletionSheet == true) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    if (showProfileComeletionSheet) {
        CompleteBusinessProfile(
            sheetState = sheetState,
            expandPercentage = expandPercentage,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteBusinessProfile(
    sheetState: SheetState,
    expandPercentage: Float = 0.95f,
    vm: AddServiceScreenVM = viewModel()
) {

    val tag = "CompleteUserProfile"

    val fullName by vm.fullName.collectAsState()

    LaunchedEffect(Unit) {
        sheetState.show()
    }

    ModalBottomSheet(
        containerColor = AppColor.White,
        onDismissRequest = {
            Log.e(tag, "onDismissRequest: ")
            vm.onDismiss_CompleteUserProfileSheet()
        },
        sheetState = sheetState,
        contentColor = Color.Black,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .background(color = AppColor.White)
                .fillMaxWidth()
                .wrapContentHeight()
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * expandPercentage)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                CustomTextField(
                    title = FULL_NAME,
                    hint = FULL_NAME_HINT,
                    description = FULL_NAME_DESCRIPTION,
                    textValue = fullName,
                    onValueChange = { vm.onFullNameChange(it) })

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                ) {
                    AnimatedButton(
                        text = "Submit",
                        onClick = {
                            vm.onDismiss_CompleteUserProfileSheet()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                }
            }
        }
    }
}