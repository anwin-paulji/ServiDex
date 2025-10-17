package com.zontechx.servidex.ui.provider.UploadServiceImage

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.zontechx.servidex.R
import com.zontechx.servidex.Screen
import com.zontechx.servidex.customPackage.cropkit.CropController
import com.zontechx.servidex.customPackage.cropkit.CropOptions
import com.zontechx.servidex.customPackage.cropkit.CropRatio
import com.zontechx.servidex.customPackage.cropkit.CropShape
import com.zontechx.servidex.customPackage.cropkit.GridLinesType
import com.zontechx.servidex.customPackage.cropkit.GridLinesVisibility
import com.zontechx.servidex.customPackage.cropkit.ImageCropper
import com.zontechx.servidex.customPackage.cropkit.rememberCropController
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.data.static.AppKey
import com.zontechx.servidex.model.sealed.ApiResponse
import com.zontechx.servidex.model.sealed.UiEvent
import com.zontechx.servidex.ui.components.AppBottomSheet
import com.zontechx.servidex.ui.provider.UploadServiceImage.component.AddServiceSuccess
import com.zontechx.servidex.ui.provider.UploadServiceImage.component.BottomBar
import com.zontechx.servidex.ui.provider.UploadServiceImage.component.HorizontalImageList
import com.zontechx.servidex.ui.provider.UploadServiceImage.component.Toolbar
import com.zontechx.servidex.ui.provider.UploadServiceImage.vm.UploadServiceImageVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.utils.InAppStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadServiceImageScreen(
    serviceId: String,
    source: String,
    rootNavController: NavHostController
) {
    val TAG = "UploadServiceImageScreen"
    val context = LocalContext.current
    val vm: UploadServiceImageVM = viewModel()
    val responseState by vm.uploadImageListResponse.collectAsState()
//    val sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = true,
//        confirmValueChange = { newValue ->
//            // Return false if user tries to drag down to hidden
//            // Return true if programmatic changes are allowed
//            newValue != SheetValue.Hidden
//        })

    // Create a sheet state that disables drag gestures
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            false
        }
    )

    var showSheet by remember { mutableStateOf(false) }
    vm.setServiceId(serviceId)

    LaunchedEffect(sheetState) {
        if (showSheet == true) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    when (responseState) {
        is ApiResponse.Success -> {
            showSheet = true
        }

        else -> {}
    }

    fun onDismiss() {
        showSheet = false
    }

    fun handleOnSheetCloseButtonPress() {
        showSheet = false

        val lastLaunchedScreen = InAppStore.getString(context, key = AppKey.LAST_LAUNCHED_SCREEN)
        if (lastLaunchedScreen.isNullOrEmpty()
                .not() && lastLaunchedScreen == AppConstant.ScreenName.PORVIDER_HOME_SCREEN
        ) {
            vm.popTo(Screen.ProviderDashboardScreen.route)
        } else {
            vm.popTo(Screen.MainScreen.route)
        }
    }

    BackHandler { showSheet = true }

    LaunchedEffect(Unit) {
        vm.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> rootNavController.navigate(event.route)
                is UiEvent.PopUpTo -> {
                    rootNavController.popBackStack(event.route, inclusive = event.inclusive)
                }

                UiEvent.PopBackStack -> rootNavController.popBackStack()
                else -> {}
            }
        }
    }

    UploadServiceImagePreview { showSheet = true }

    if (showSheet) {
        AppBottomSheet(sheetState = sheetState, onDismiss = { onDismiss() }) {
            AddServiceSuccess { handleOnSheetCloseButtonPress() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UploadServiceImagePreview(onBackClick: () -> Unit = {}) {
    val TAG = "UploadServiceImageScreen"
    val context = LocalContext.current
    val vm: UploadServiceImageVM = viewModel()
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )

    fun onClickButton() {
        coroutineScope.launch {
            if (pagerState.currentPage < pagerState.pageCount - 1) {
                if (vm.selectedImageList.value.size >= 2) {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                } else {
                    Toast.makeText(
                        context,
                        "You need to select at least 2 images to proceed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                vm.uploadImageTrigger()
            }
        }
    }

    Scaffold(
        topBar = {
            Toolbar(
                modifier = Modifier
                    .statusBarsPadding()
            ) {
                onBackClick()
            }
        },
        bottomBar = {
            BottomBar { onClickButton() }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.White)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                beyondViewportPageCount = 2,
                modifier = Modifier.weight(1f),
                key = { page -> page }
            ) { page ->
                Box {
                    when (page) {
                        0 -> Page1()
                        1 -> Page2()
                    }
                }

            }
        }
    }
}

@Composable
fun Page1() {
    Column {
        HorizontalImageList()
        PreviewEditImage(modifier = Modifier.weight(1f))
    }
}

@Composable
fun Page2() {
    val vm: UploadServiceImageVM = viewModel()
    val selectedImage by vm.selectedImageList.collectAsState()

    fun handleImageSelection(index: Int) {
        vm.setCoverImage(index)
    }

    Column(
        modifier = Modifier.padding(start = 30.dp, end = 30.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            text = AppConstant.SELECT_COVER_IMAGE, style = TextStyle(
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(
                    Font(R.font.roboto_semibold)
                )
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(selectedImage.size) { index ->

                val isSelected = selectedImage[index].isCoverImage

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = if (isSelected) 4.dp else 0.dp,
                            color = AppColor.Primary,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { handleImageSelection(index) }
                        .background(AppColor.white_2)
                ) {

                    Image(
                        bitmap = selectedImage[index].bitmapImage.asImageBitmap(),
                        contentDescription = "Selected image ${index + 1}",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.Crop
                    )

                    if (isSelected) {
                        Text(
                            text = "Cover Image",
                            style = TextStyle(
                                color = AppColor.White,
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.montserrat_bold))
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                .background(color = AppColor.Primary)
                                .padding(start = 4.dp, end = 4.dp, bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PreviewEditImage(modifier: Modifier = Modifier) {
    val TAG = "PreviewEditImage"
    val vm: UploadServiceImageVM = viewModel()
    val selectedImage by vm.selectedImageList.collectAsState()
    val pagerIndex by vm.pagerIndex.collectAsState()
    val uploadTrigger by vm.uploadTrigger.collectAsState()

    val cropOption = CropOptions(
        cropShape = CropShape.AspectRatio(CropRatio.SQUARE), // Or try Oval/FreeForm to see if it changes scaling
        contentScale = ContentScale.Fit, // Try Fit instead of Crop to better handle tall images
        gridLinesVisibility = GridLinesVisibility.NEVER, // Optional: Simplify UI
        gridLinesType = GridLinesType.GRID,
        handleRadius = 8.dp,
        touchPadding = 16.dp
    )

    val cropControllers: List<CropController> = selectedImage.map {
        rememberCropController(
            cropOptions = cropOption,
            bitmap = it.bitmapImage
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { selectedImage.size } // <-- dynamic based on list size
    )

    LaunchedEffect(uploadTrigger) {
        if (uploadTrigger == true) {
            val croppedImages = cropControllers.map { it.crop() }
            vm.setCroppedImage(croppedImages)
        }
    }

    LaunchedEffect(pagerIndex) {
        pagerState.scrollToPage(pagerIndex)
    }

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = true,
        beyondViewportPageCount = 6, // optional
        modifier = modifier.fillMaxWidth(),
        key = { page ->
            selectedImage.getOrNull(page)?.originalImageUri ?: page
        }
    ) { page ->
        selectedImage.getOrNull(page)?.let { imageData ->
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AppColor.white_2)
            ) {
                val controller = cropControllers[page]
                ImageCropper(
                    modifier = Modifier.fillMaxSize(),
                    cropController = controller
                )
            }
        }
    }
}