package com.zontechx.servidex.ui.provider.addServiceScreen.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage

@Composable
fun LoadUrlImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillBounds,
) {
    val TAG = "LoadUrlImage"

    var context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context).build()

    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier,
        imageLoader = imageLoader
    )
}