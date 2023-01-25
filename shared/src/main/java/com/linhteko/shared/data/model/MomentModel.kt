package com.linhteko.shared.data.model

import com.linhteko.shared.data.vos.PostVO

interface MomentModel {
    fun uploadMoment(moment: PostVO, onSuccess: () -> Unit, onFailure: (String) -> Unit)

    fun getMoments(onSuccess: (List<PostVO>) -> Unit, onFailure: (String) -> Unit)
    fun reactFavourite(
        postId: Long,
        userId: String?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun removeFavourite(
        postId: Long,
        userId: String?,
        function: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getMomentsBookmarkByUser(
        userId: String,
        onSuccess: (List<PostVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun bookmarkByUser(
        userId: String,
        postVO: PostVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun removeBookmarkByUser(
        userId: String,
        postVO: PostVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )
}