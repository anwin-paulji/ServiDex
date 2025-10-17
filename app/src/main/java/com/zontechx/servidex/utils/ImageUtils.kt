package com.zontechx.servidex.utils


import android.graphics.ImageDecoder
import java.io.ByteArrayOutputStream
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.createBitmap

object ImageUtils {
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun urisToBitmaps(context: Context, uris: List<Uri>): List<Bitmap> {
        val bitmaps = mutableListOf<Bitmap>()
        uris.forEach { uri ->
            try {
                val bitmap = normalizeBitmap(context, uri)
                bitmaps.add(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bitmaps
    }

    fun normalizeBitmap(context: Context, uri: Uri): Bitmap {
        val source = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        Log.d("ImageUtils", "normalizeBitmap: ${source.width}x${source.height}")
        return createBitmap(source.width, source.height).apply {
            val canvas = Canvas(this)
            canvas.drawBitmap(source, 0f, 0f, null)
        }
    }

    fun bitmapToJpegByteArray(bitmap: Bitmap, maxSizeKB: Int = 400): ByteArray {
        val maxSize = maxSizeKB * 1024
        var quality = 100
        var byteArray: ByteArray

        do {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            byteArray = outputStream.toByteArray()
            quality -= 5 // reduce quality step by step
        } while (byteArray.size > maxSize && quality > 20) // stop at 20% to avoid unusable quality

        return byteArray
    }

    fun bitmapsToJpegByteArrays(bitmaps: List<Bitmap>, maxSizeKB: Int = 400): List<ByteArray> {
        return bitmaps.map { bitmap ->
            bitmapToJpegByteArray(bitmap, maxSizeKB)
        }
    }
}