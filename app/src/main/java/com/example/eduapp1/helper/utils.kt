package com.example.eduapp.helper

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.IOException

fun getBitmapFromAssetsByIndex(context: Context, level: String, index: Int): ImageBitmap? {
    return try {
        // List all files in the specific sub-folder (e.g., "1")
        val files = context.assets.list(level)

        // Check if files exist and index is within bounds
        if (files != null && index >= 0 && index < files.size) {
            val fileName = files[index]
            val fullPath = "$level/$fileName"

            // Open and decode the stream
            context.assets.open(fullPath).use { inputStream ->
                BitmapFactory.decodeStream(inputStream).asImageBitmap()
            }
        } else {
            null
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
