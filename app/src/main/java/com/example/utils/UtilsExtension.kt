package com.example.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.math.roundToInt
import kotlin.math.sqrt


fun Context.hideKeyboardManager(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun setRequiredReadStoragePermission(): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
        )
    else
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
}

fun reduceBitmapSize(bitmap: Bitmap, MAX_SIZE: Int): Bitmap? {
    val ratioSquare: Double
    val bitmapHeight: Int = bitmap.height
    val bitmapWidth: Int = bitmap.width
    ratioSquare = bitmapHeight * bitmapWidth / MAX_SIZE.toDouble()
    if (ratioSquare <= 1) return bitmap
    val ratio = sqrt(ratioSquare)
    val requiredHeight = (bitmapHeight / ratio).roundToInt()
    val requiredWidth = (bitmapWidth / ratio).roundToInt()
    return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
}


 fun getBitmapFromUri(selectedPhotoUri: Uri, context: Context): Bitmap {
    val bitmap = when {
        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
            context.contentResolver,
            selectedPhotoUri
        )
        else -> {
            val source = ImageDecoder.createSource(context.contentResolver, selectedPhotoUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
    return bitmap
}

