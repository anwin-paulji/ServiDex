package com.zontechx.servidex.ui.screens.nearByServiceScreen.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.zontechx.servidex.R
import com.zontechx.servidex.data.static.AppConstant
import com.zontechx.servidex.model.response.ServiceDetailsResponse
import com.zontechx.servidex.ui.components.AnimatedComponent
import com.zontechx.servidex.ui.theme.AppColor


@Preview
@Composable
fun NearByServiceListItem(
    context: Context = LocalContext.current,
    nearByServiceItem: ServiceDetailsResponse = ServiceDetailsResponse(_id ="123", serviceTitle = "Testing Cleaning Service"),
    onClick: () -> Unit = {},
) {
    val imageLoader = ImageLoader.Builder(context).build()

    val priceType =
        if (nearByServiceItem.chargeBasis?.pricingType == AppConstant.PricingType.MINUTES) {
            "Minutes"
        } else {
            "Hours"
        }

    val serviceImage = nearByServiceItem.serviceImages?.find { it.isPrimary == true }?.croppedUrl

    AnimatedComponent(
        onClick = {
            onClick()
        }) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = AppColor.pink_1,
                    spotColor = AppColor.gray_1
                )
                .background(AppColor.White, shape = RoundedCornerShape(12.dp))
        ) {

            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // IMAGE
                if (serviceImage != null) {
                    AsyncImage(
                        model = serviceImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(160.dp)
                            .fillMaxWidth(),
                        imageLoader = imageLoader
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.service_default),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(160.dp)
                            .fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = nearByServiceItem.primaryCategory!!.categoryName,
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W700)
                        )
                        Text(
                            text = "⭐ 4.5", style = TextStyle(
                                textAlign = TextAlign.End,
                                fontSize = 12.sp,
                                color = AppColor.gray,
                                fontWeight = FontWeight.W400
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = nearByServiceItem.serviceTitle,
                        minLines = 2,
                        maxLines = 2,
                        style = TextStyle(
                            fontSize = 14.sp, color = AppColor.gray, fontWeight = FontWeight.W400
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row(
                            modifier = Modifier.padding(top = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "₹${nearByServiceItem.chargeBasis?.price ?: "00.00"}",
                                style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                                    fontSize = 20.sp,
                                    color = AppColor.Black
                                )
                            )
                            Text(
                                text = " /${priceType}", style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.roboto_semibold)),
                                    fontSize = 16.sp,
                                    color = AppColor.gray_4
                                )
                            )
                        }

                    }
                }
            }
        }
    }
}