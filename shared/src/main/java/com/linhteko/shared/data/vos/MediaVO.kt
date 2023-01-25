package com.linhteko.shared.data.vos

import android.net.Uri


data class MediaVO(
    val uri: Uri? = null,
    val type: String
){
    companion object{
        const val MEDIA_TYPE_PHOTO = "media-photo"
        const val MEDIA_TYPE_VIDEO = "media-video"
        const val MEDIA_TYPE_ADD = "add-media"
    }
}
