package com.linhteko.shared.network.firebase

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.linhteko.shared.network.MediaStorage
import java.io.ByteArrayOutputStream

object FirebaseStorageImpl : MediaStorage {
    private var storage = Firebase.storage
    private var reference = storage.reference

    override fun uploadMedia(
        bitmap: Bitmap,
        onUploaded: (String) -> Unit,
        onFailure: (String) -> Unit,
        root: String
    ) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        val imgRef = reference.child("$root/${System.currentTimeMillis()}")

        imgRef.putBytes(outputStream.toByteArray())
            .continueWithTask {
                return@continueWithTask imgRef.downloadUrl
            }.addOnSuccessListener {
                onUploaded(it.toString())
            }.addOnFailureListener {
                onFailure(it.localizedMessage ?: "Something Wrong")
            }
    }

    override fun uploadMedia(
        uri: Uri,
        onUploaded: (String) -> Unit,
        onFailure: (String) -> Unit,
        root: String
    ) {

        val ref = reference.child("content-videos/${System.currentTimeMillis()}")
        ref.putFile(uri).continueWithTask {
            return@continueWithTask ref.downloadUrl
        }.addOnSuccessListener {
            onUploaded(it.toString())
        }.addOnFailureListener {
            onFailure(it.localizedMessage ?: "Something Wrong")
        }
    }
}