package com.zontechx.servidex.ui.provider.providerDashboard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zontechx.servidex.LocalRootNavController
import com.zontechx.servidex.LocalSharedViewModel
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.theme.AppColor
import com.zontechx.servidex.ui.vm.SharedVM
import com.zontechx.servidex.utils.AppConfig

@Composable
fun ProviderHomeToolBar(onProfileClick: () -> Unit = {}) {

    val rootNavController = LocalRootNavController.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AppColor.white_2)
            .statusBarsPadding()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
        ) {

            ToolbarStart(onProfileClick = { onProfileClick() })
        }
    }
}

@Composable
fun ToolbarStart(onProfileClick: () -> Unit = {}) {
    val TAG = "ToolbarStart"
    var context = LocalContext.current
    var profileImage = AppConfig.APP_LOGG_URL
    val imageLoader = ImageLoader.Builder(context).build()
    var userName by remember { mutableStateOf("") }
    val sharedVM: SharedVM = LocalSharedViewModel.current
    val currentAddress = sharedVM.currentAddress.collectAsState()

    Row {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(profileImage)
                .crossfade(true)
                .build(),
            contentDescription = "Image from URL",
            imageLoader = imageLoader,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .size(40.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .clickable {
                    onProfileClick()
                },
            contentScale = ContentScale.Crop
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .clickable {}
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
        ) {
            if (userName.isNotEmpty()) {
                Text(
                    text = "Hi ${userName}",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                        fontSize = 16.sp
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.location),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )

                currentAddress.value?.let { address ->
                    Text(
                        text = address.locationName,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = AppColor.Black,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        )
                    )
                }

                Image(
                    painter = painterResource(R.drawable.arrow_down),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ToolBarPreview() {
    ProviderHomeToolBar(onProfileClick = {})
}