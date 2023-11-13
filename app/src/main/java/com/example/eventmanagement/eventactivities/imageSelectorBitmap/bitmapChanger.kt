package com.example.eventmanagement.eventactivities.imageSelectorBitmap

//class bitmapChanger {
//}

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.IOException

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    try {
        // Use the content resolver to open the input stream from the URI
        val inputStream = context.contentResolver.openInputStream(uri)

        // Decode the input stream into a Bitmap
        return BitmapFactory.decodeStream(inputStream)

    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}