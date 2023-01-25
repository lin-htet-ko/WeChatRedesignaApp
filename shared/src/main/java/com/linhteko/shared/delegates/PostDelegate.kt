package com.linhteko.shared.delegates

interface PostDelegate: PostHeaderDelegate, PostReactionDelegate {

    fun onTapContent(postId: Int)
}