package com.linhteko.shared.data.model

import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.network.ApiService
import com.linhteko.shared.network.firebase.FirebaseApiService

object MomentModelImpl: MomentModel {

    private val apiService: ApiService = FirebaseApiService

    override fun uploadMoment(moment: PostVO, onSuccess: () -> Unit, onFailure: (String) -> Unit){
        apiService.uploadMoment(moment, onSuccess, onFailure)
    }

    override fun getMoments(onSuccess: (List<PostVO>) -> Unit, onFailure: (String) -> Unit) {
        apiService.getPost(
            onSuccess,
            onFailure
        )
    }

    override fun reactFavourite(postId: Long, userId: String?, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        apiService.reactFav(postId, userId, onSuccess, onFailure)
    }

    override fun removeFavourite(
        postId: Long,
        userId: String?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        apiService.removeFav(postId, userId, onSuccess, onFailure)
    }

    override fun getMomentsBookmarkByUser(
        userId: String,
        onSuccess: (List<PostVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        apiService.getMomentsBookmarkByUser(userId,onSuccess,onFailure)
    }

    override fun bookmarkByUser(
        userId: String,
        postVO: PostVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        apiService.bookmarkByUser(userId, postVO, onSuccess, onFailure)
    }

    override fun removeBookmarkByUser(
        userId: String,
        postVO: PostVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        apiService.removeBookmarkByUser(userId, postVO, onSuccess, onFailure)
    }
}