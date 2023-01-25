package com.linhteko.shared.mvp.presenters

import android.graphics.Bitmap
import android.net.Uri
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.ConversationView

interface ConversationPresenter: BasePresenter<ConversationView> {

    var user: UserVO?

    fun getConversation(uid: String)
    fun sendMessage(uid: String, message: ConversationVO)
    fun sendImageMessage(bitmap: Bitmap, uid: String, otherUserVO: UserVO)
    fun sendVideoMessage(uri: Uri, userId: String, otherUser: UserVO)
    fun getGroup(groupId: String)
    fun sendGroupMessage(groupId: String, conversationVO: ConversationVO)
    fun getGroupConversations(groupId: String)
    fun sendImageGroupMessage(bitmap: Bitmap, userId: String, groupId: String)
}