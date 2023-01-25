package com.linhteko.shared.network

import com.linhteko.shared.data.vos.PostVO

interface ApiService {

    fun uploadMoment(moment: PostVO, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun getPost(onSuccess: (List<PostVO>) -> Unit, onFailure: (String) -> Unit)
    fun reactFav(postId: Long, userId: String?, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun removeFav(postId: Long, userId: String?, onSuccess: () -> Unit, onFailure: (String) -> Unit)
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