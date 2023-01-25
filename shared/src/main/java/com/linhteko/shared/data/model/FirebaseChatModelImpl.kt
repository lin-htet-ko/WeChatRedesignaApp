package com.linhteko.shared.data.model

import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.network.ChatService
import com.linhteko.shared.network.firebase.FirebaseChatService

object FirebaseChatModelImpl: ChatModel {

    private val chatService: ChatService = FirebaseChatService

    override fun getConversations(
        currentUser: String,
        otherUser: String,
        onSuccess: (List<ConversationVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        chatService.getConversations(currentUser, otherUser, onSuccess, onFailure)
    }

    override fun sendMessage(
        currentUser: String,
        otherUser: String,
        message: ConversationVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        chatService.sendMessage(currentUser, otherUser, message, onSuccess, onFailure)
    }

    override fun createGroup(groupVO: GroupVO, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        chatService.createGroup(groupVO, onSuccess, onFailure)
    }

    override fun getGroups(
        userId: String,
        onSuccess: (List<GroupVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        chatService.getGroups(userId, onSuccess, onFailure)
    }

    override fun getGroup(
        groupId: String,
        onSuccess: (GroupVO?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        chatService.getGroup(groupId, onSuccess, onFailure)
    }

    override fun sendGroupMessage(
        groupId: String,
        conversationVO: ConversationVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        chatService.sendGroupMessage(groupId, conversationVO, onSuccess, onFailure)
    }

    override fun getGroupConversations(
        groupId: String,
        onSuccess: (List<ConversationVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        chatService.getGroupConversations(groupId, onSuccess, onFailure)
    }

    override fun getContactsAndMessages(
        uid: String,
        onSuccess: (List<String>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        chatService.getChatContacts(uid, onSuccess, onFailure)
    }

    override fun getLastConversation(
        currentUser: String,
        otherUser: String,
        onSuccess: (ConversationVO?) -> Unit,
        onFailure: (String) -> Unit
    ){
        chatService.getLastConversation(currentUser, otherUser, onSuccess, onFailure)
    }
}