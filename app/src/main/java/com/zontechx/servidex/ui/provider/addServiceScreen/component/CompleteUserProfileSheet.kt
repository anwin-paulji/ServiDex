package com.zontechx.servidex.ui.provider.addServiceScreen.component

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zontechx.servidex.data.model.response.UpdateUserProfileResponse
import com.zontechx.servidex.data.static.AppConstant.FULL_NAME
import com.zontechx.servidex.data.static.AppConstant.FULL_NAME_DESCRIPTION
import com.zontechx.servidex.data.static.AppConstant.FULL_NAME_HINT
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.components.AnimatedButton
import com.zontechx.servidex.ui.provider.addServiceScreen.vm.AddServiceScreenVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.utils.InAppStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompleteUserProfileSheet() {

    var tag = "CompleteUserProfileSheet"

    val vm: AddServiceScreenVM = viewModel()
    val context = LocalContext.current
    val showProfileComeletionSheet by vm.showProfileComeletionSheet.collectAsState()

    var expandPercentage = 0.9f

    var sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true })

    LaunchedEffect(Unit) {
        val isProfileComplete = InAppStore.getBoolean(context, AppKey.IS_PROFILE_COMPLETE)
        if(isProfileComplete == false) {
            vm.showProfileCompletionBottomSheet(true)
        }
    }

    LaunchedEffect(key1 = showProfileComeletionSheet) {
        if (showProfileComeletionSheet == true) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    if (showProfileComeletionSheet == true) {
        CompleteUserProfile(
            sheetState = sheetState,
            expandPercentage = expandPercentage,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CompleteUserProfile(
    sheetState: SheetState = rememberModalBottomSheetState(),
    expandPercentage: Float = 0.95f,
    vm: AddServiceScreenVM = viewModel()
) {

    val fullName by vm.fullName.collectAsState()
    var progress by remember { mutableStateOf<Boolean>(false) }
    val updatedUserProfileResponse by vm.updatedUserProfileResponse.collectAsState()
    val context = LocalContext.current

    val titlestyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = AppColor.Black,
        textAlign = TextAlign.Center,
        letterSpacing = 0.5.sp
    )

    LaunchedEffect(updatedUserProfileResponse) {
        when (updatedUserProfileResponse) {
            null -> {}
            is ApiResponse.Initial -> {}

            is ApiResponse.Loading -> { progress = true }

            is ApiResponse.Success -> {
                progress = false
                InAppStore.saveBoolean(context, AppKey.IS_PROFILE_COMPLETE, (updatedUserProfileResponse as ApiResponse.Success<UpdateUserProfileResponse>).data.isProfileCompleted)
                vm.onDismiss_CompleteUserProfileSheet()
            }

            is ApiResponse.Error -> {
                progress = false
                Toast.makeText(context, (updatedUserProfileResponse as ApiResponse.Error).message, Toast.LENGTH_SHORT).show()
                vm.onDismiss_CompleteUserProfileSheet()
            }
        }
    }

    ModalBottomSheet(
        containerColor = AppColor.White,
        onDismissRequest = {
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
                .verticalScroll(rememberScrollState())
                .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * expandPercentage)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Complete your profile",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp),
                    style = titlestyle)

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

                    if(progress) {
                        CircularProgressIndicator(
                            trackColor = AppColor.Primary,
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.Center)
                        )
                    } else {
                        AnimatedButton(
                            text = "Complete profile",
                            onClick = {
                                vm.updateUserProfile()
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
}