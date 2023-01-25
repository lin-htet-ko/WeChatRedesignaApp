package com.linhteko.shared.data.vos

data class GroupVO(
    val id: Long = System.currentTimeMillis(),
    val name: String? = null,
    val image: String? = null,
    val members: List<String> = listOf(),
    val messages: HashMap<String, ConversationVO> = hashMapOf(),
    val type: String = TYPE_GROUP_PEOPLE.second
){
    companion object{
        val TYPE_GROUP_ADD = 1 to "group-to-create-new"
        val TYPE_GROUP_PEOPLE = 2 to "people-group"
    }
}
