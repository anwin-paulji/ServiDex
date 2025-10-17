package com.zontechx.servidex.ui.provider.addServiceScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.zontechx.servidex.R
import com.zontechx.servidex.data.model.response.CategoryListItem
import com.zontechx.servidex.model.sealed.CategoryItemModel
import com.zontechx.servidex.ui.components.AnimatedComponent
import com.zontechx.servidex.ui.theme.AppColor

@Composable
fun CategoryItem(
    data: CategoryListItem = CategoryListItem("Painting"),
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    var context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).build()
    val categoryName = data.categoryName?.replace("Services", "", ignoreCase = true)?.trim()

    AnimatedComponent(
        onClick = { onClick() },
        content = {
            Box(
                modifier = modifier
                    .size(100.dp)
                    .background(AppColor.White)
                    .border(
                        width = if (data.isSelected == true) 2.dp else 0.dp,
                        color = if (data.isSelected == true) AppColor.Primary else AppColor.White,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(data.categoryUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Image from URL",
                        imageLoader = imageLoader,
                        modifier = Modifier
                            .size(65.dp)
                            .padding(top = 8.dp)
                            .clip(shape = RoundedCornerShape(50.dp))
                            .background(color = AppColor.yellow_1),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = categoryName?:"",
                        modifier = Modifier
                            .padding(top = 4.dp),
                        minLines = 2,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.roboto_medium)),
                            color = AppColor.gray_4,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun CategoryItemPreview() {

    val categoryList = remember {
        mutableStateOf(
            listOf(
                CategoryItemModel(
                    "Painting",
                    "https://www.pngall.com/wp-content/uploads/11/Painting-Service-PNG-Image.png"
                ),
                CategoryItemModel("Cleaning", ""),
            )
        )
    }

    CategoryItem(
        data = CategoryListItem("Painting"),
        onClick = {})
}