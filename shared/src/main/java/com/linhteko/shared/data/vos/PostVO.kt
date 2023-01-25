package com.linhteko.shared.data.vos

import java.util.UUID

data class PostVO(
    val postId: Long = 0,
    val userName: String? = null,
    val userImg: String? = null,
    val userId: String? = null,
    val uploadTime: Long = 0,
    val description: String? = null,
    val contentImages: List<ImageVO> = listOf(),
    val contentVideo: VideoVO? = null,
    val likes: List<UserVO> = listOf(),
    val bookmarks: List<String> = listOf(),
    val postType: String? = POST_TYPE_IMAGE
) {

    companion object{
        const val POST_TYPE_IMAGE = "image_post"
        const val POST_TYPE_VIDEO = "video_post"
    }
}