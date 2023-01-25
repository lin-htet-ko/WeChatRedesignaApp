package com.linhteko.shared.network

import android.graphics.Bitmap
import android.net.Uri

interface MediaStorage {

    fun uploadMedia(
        bitmap: Bitmap,
        onUploaded: (String) -> Unit,
        onFailure: (String) -> Unit,
        root: String = "profile-images"
    )
    fun uploadMedia(uri: Uri, onUploaded: (String) -> Unit, onFailure: (String) -> Unit, root: String= "profile-media")
}