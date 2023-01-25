package com.linhteko.shared.data.model

import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.data.vos.UserVO

interface ChatModel {

    fun getConversations(
        currentUser: String,
        otherUser: String,
        onSuccess: (List<ConversationVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun sendMessage(
        currentUser: String,
        otherUser: String,
        message: ConversationVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun createGroup(
        groupVO: GroupVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getGroups(
        userId: String,
        onSuccess: (List<GroupVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun getGroup(
        groupId: String,
        onSuccess: (GroupVO?) -> Unit,
        onFailure: (String) -> Unit
    )

    fun sendGroupMessage(
        groupId: String,
        conversationVO: ConversationVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getGroupConversations(
        groupId: String,
        onSuccess: (List<ConversationVO>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun getContactsAndMessages(
        uid: String,
        onSuccess: (List<String>) -> Unit,
        onFailure: (String) -> Unit
    )

    fun getLastConversation(
        currentUser: String,
        otherUser: String,
        onSuccess: (ConversationVO?) -> Unit,
        onFailure: (String) -> Unit
    )
}