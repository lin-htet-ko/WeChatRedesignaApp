package com.linhteko.shared.mvp.views

import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.base.BaseView

interface ConversationView: BaseView {
    fun renderConversations(conversations: List<ConversationVO>)
    fun renderGroupUI(groupVO: GroupVO)
    fun validateAndSendGroupMessage(groupId: String, currentUser: UserVO)
}
