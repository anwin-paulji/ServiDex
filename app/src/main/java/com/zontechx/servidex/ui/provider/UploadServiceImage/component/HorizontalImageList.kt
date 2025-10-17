package com.zontechx.servidex.ui.provider.UploadServiceImage.component

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.provider.UploadServiceImage.vm.UploadServiceImageVM
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.utils.ImageUtils

@Preview
@Composable
fun HorizontalImageList() {

    val vm: UploadServiceImageVM = viewModel()
    val maxItems = 6
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).build()
    val selectedImageList by vm.selectedImageList.collectAsState()
    val pagerIndex by vm.pagerIndex.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        val bitmaplist: List<Bitmap> = ImageUtils.urisToBitmaps(context, uris)
        vm.addImageList(uris, bitmaplist)
    }

    fun handleImageClick(index: Int) {
        if (index <= selectedImageList.size - 1) {
            vm.switchToImage(index)
        } else {
            launcher.launch("image/*")
        }
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColor.White)
    ) {
        items(maxItems) { index ->
            val hasImage = index < selectedImageList.size
            val imageUri: Uri? = if (hasImage) selectedImageList[index].originalImageUri else null
            val isSelected = (pagerIndex == index && hasImage)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = if(isSelected) 4.dp else 0.dp,                     // stroke thickness
                        color = AppColor.Primary,          // stroke color
                        shape = RoundedCornerShape(16.dp) // must match your clip shape
                    )
                    .clickable { handleImageClick(index) }
                    .background(AppColor.white_2)
            ) {
                if (imageUri != null) {
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(imageUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Selected image ${index + 1}",
                            imageLoader = imageLoader,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove image",
                            tint = AppColor.white_2,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(AppColor.Primary)
                                .align(Alignment.TopEnd)
                                .padding(4.dp)
                                .size(16.dp)
                                .clickable {
                                    vm.removeImage(selectedImageList[index])
                                }
                        )
                    }

                } else {
                    Icon(
                        painter = painterResource(R.drawable.upload_ic_24),
                        contentDescription = "Upload image",
                        tint = AppColor.gray_4
                    )
                }
            }
        }
    }
}
