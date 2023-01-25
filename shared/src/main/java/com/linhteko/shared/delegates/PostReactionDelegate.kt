package com.linhteko.shared.delegates

interface PostReactionDelegate {
    fun onTapFav(postId: Long)
    fun onTapComment(postId: Long)
    fun onTapBookmark(postId: Long)
}