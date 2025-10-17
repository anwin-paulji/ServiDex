package com.zontechx.servidex.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.data.model.response.UpdateUserProfileResponse
import com.zontechx.servidex.data.static.AppConstant.FULL_NAME
import com.zontechx.servidex.data.static.AppConstant.FULL_NAME_DESCRIPTION
import com.zontechx.servidex.data.static.AppConstant.FULL_NAME_HINT
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.ui.provider.addServiceScreen.component.CustomTextField
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM
import com.zontechx.servidex.utils.InAppStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UpdateProfileSheet() {

    var sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val sharedVM: SharedVM = LocalSharedViewModel.current
    val showProfileComeletionSheet by sharedVM.showProfileComeletionSheet.collectAsStateWithLifecycle()

    if (showProfileComeletionSheet) {
        UpdateProfileBottomSheet(sheetState = sheetState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UpdateProfileBottomSheet(sheetState: SheetState = rememberModalBottomSheetState()) {
    val TAG = "UpdateProfileBottomSheet"
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sharedVM: SharedVM = LocalSharedViewModel.current
    val fullName by sharedVM.fullName.collectAsState()
    var progress by remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(Unit) {
        sharedVM.updatedUserProfileResponse.collect { response ->
                when(response) {
                    is ApiResponse.Initial -> {}

                    is ApiResponse.Loading -> {
                        progress = true
                    }

                    is ApiResponse.Success -> {
                        progress = false
                        InAppStore.saveBoolean(
                            context,
                            AppKey.IS_PROFILE_COMPLETE,
                            response.data.isProfileCompleted
                        )
                        scope.launch {
                            Log.e(TAG, "#4 Hidding")
                            sheetState.hide()
                            sharedVM.showProfileCompletionBottomSheet(false)
                        }
                    }

                    is ApiResponse.Error -> {
                        progress = false
                        Toast.makeText(
                            context,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        scope.launch {
                            sheetState.hide()
                            sharedVM.showProfileCompletionBottomSheet(false)
                        }
                    }
                }
            }
    }

    ModalBottomSheet(
        containerColor = AppColor.White,
        onDismissRequest = { sharedVM.showProfileCompletionBottomSheet(false) },
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
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AppColor.Black,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                )

                CustomTextField(
                    title = FULL_NAME,
                    hint = FULL_NAME_HINT,
                    description = FULL_NAME_DESCRIPTION,
                    textValue = fullName,
                    onValueChange = { sharedVM.onFullNameChange(it) })

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                ) {

                    if (progress) {
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
                                sharedVM.updateUserProfile()
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