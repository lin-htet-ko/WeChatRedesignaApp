package com.linhteko.shared.data.vos

data class ConversationVO(
    val id: Long = System.currentTimeMillis(),
    val message: String? = null,
    val name: String? = null,
    val profilePic: String? = null,
    val userId: String? = null,
    val file: String? = null,
    val type: String = CONVERSATION_TYPE_TEXT.second
){
    companion object{
        val CONVERSATION_TYPE_TEXT = 0 to "text"
        val CONVERSATION_TYPE_IMAGE = 1 to "image"
        val CONVERSATION_TYPE_VIDEO = 3 to "video"
    }
}
