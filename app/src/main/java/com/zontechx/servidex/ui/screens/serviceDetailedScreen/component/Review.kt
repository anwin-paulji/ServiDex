package com.zontechx.servidex.ui.screens.serviceDetailedScreen.component

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zontechx.servidex.R
import com.zontechx.servidex.ui.screens.mainScreen.page.BookingScreen.BookingItem
import com.zontechx.servidex.ui.screens.mainScreen.page.BookingScreen.vm.BookingVM
import com.zontechx.servidex.ui.screens.serviceDetailedScreen.vm.ServiceDetailedVM
import com.zontechx.servidex.ui.theme.AppColor

data class Review(
    val id: String,
    val name: String,
    val rating: Int,
    val comment: String
)

@Composable
fun Review() {
    val TAG = "Review"

    val vm: ServiceDetailedVM = viewModel()
    val listState = rememberLazyListState()
    val bookings by vm.reviewsList.collectAsState()
    val hasMore by vm.hasMore
    val isLoadingMore by vm.isLoadingMore

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.White)
    ) {
        items(bookings, key = { it._id }) { booking ->
            ReviewItem()
        }

        if (isLoadingMore) {
            item {
                CircularProgressIndicator(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }

    // 👇 Detect scroll end and load more
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex == bookings.lastIndex && hasMore && !isLoadingMore) {
                    vm.loadReviewMore()
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewItem(
    data: Review = Review("1", "John Doe", 4, "Great service!"),
    context: Context = LocalContext.current
) {
    val profileImage = "https://www.gravatar.com/avatar/2c7d99fe281ecd3bcd65ab915bac6dd5?s=250"
    val imageLoader = ImageLoader.Builder(context).build()

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(profileImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = "User profile image",
                    imageLoader = imageLoader,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .size(35.dp)
                        .clip(shape = RoundedCornerShape(30.dp))
                        .background(color = AppColor.yellow_1),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = data.name,
                    style = TextStyle(
                        fontSize = 17.sp,
                        color = AppColor.Black,
                        fontFamily = FontFamily(Font(R.font.roboto_medium))
                    )
                )
            }

            LazyRow {
                items(data.rating) { _ ->
                    Text(text = "⭐")
                }
            }
        }

        Text(
            text = data.comment,
            style = TextStyle(
                fontSize = 14.sp,
                color = AppColor.Black,
                fontFamily = FontFamily(Font(R.font.roboto_regular))
            )
        )

        Divider(
            color = AppColor.gray_2,
            thickness = 0.4.dp,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))
    }
}