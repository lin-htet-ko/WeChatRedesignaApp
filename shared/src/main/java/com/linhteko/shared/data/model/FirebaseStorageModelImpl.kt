package com.linhteko.shared.data.model

import android.graphics.Bitmap
import android.net.Uri
import com.linhteko.shared.network.firebase.FirebaseStorageImpl
import com.linhteko.shared.network.MediaStorage

object FirebaseStorageModelImpl: StorageModel {

    private val storage : MediaStorage = FirebaseStorageImpl

    override fun uploadMedia(bitmap: Bitmap, onUploaded: (String) -> Unit, onFailure: (String) -> Unit, root: String) {
        storage.uploadMedia(bitmap, onUploaded, onFailure, root)
    }

    override fun uploadMedia(uri: Uri, onUploaded: (String) -> Unit, onFailure: (String) -> Unit,  root: String) {
        storage.uploadMedia(uri, onUploaded, onFailure, root)
    }
}