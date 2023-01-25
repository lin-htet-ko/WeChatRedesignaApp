package com.linhteko.shared.data.model

import android.graphics.Bitmap
import android.net.Uri

interface StorageModel {

    fun uploadMedia(bitmap: Bitmap, onUploaded: (String) -> Unit, onFailure: (String) -> Unit, root: String= "profile-images")
    fun uploadMedia(uri: Uri, onUploaded: (String) -> Unit, onFailure: (String) -> Unit, root: String= "profile-media")
}