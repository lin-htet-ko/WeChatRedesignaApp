package com.linhteko.shared.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.linhteko.shared.R

fun ImageView.loadNetworkImage(url: String, placeholder: Int = R.drawable.ic_baseline_image_24) {
    Glide.with(this).load(url).placeholder(placeholder).into(this)
}

fun Context.getWidthAndHeight(
    url: String,
    size: (width: Int, height: Int) -> Unit,
    bitmap: (Bitmap) -> Unit
) {
    Glide.with(this).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            size(
                resource.width,
                resource.height
            )
            bitmap(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {

        }
    })
}

fun Context.generateBitmap(profileUri: Uri?): Bitmap? {
    if (Build.VERSION.SDK_INT >= 29) {
        val imageSource = profileUri?.let {
            ImageDecoder.createSource(this.contentResolver, it)
        }
        if (imageSource != null) {
            return ImageDecoder.decodeBitmap(imageSource)
        }
    } else {
        return MediaStore.Images.Media.getBitmap(this.contentResolver, profileUri)
    }
    return null
}

fun generateBitmap(uri: Uri, contentResolver: ContentResolver): Bitmap? {
    if (Build.VERSION.SDK_INT >= 29) {
        val imageSource = uri?.let {
            ImageDecoder.createSource(contentResolver, it)
        }
        if (imageSource != null) {
            return ImageDecoder.decodeBitmap(imageSource)
        }
    } else {
        return MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }
    return null
}