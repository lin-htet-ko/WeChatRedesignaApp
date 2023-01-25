package com.linhteko.shared.mvp.views

import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.base.BaseView

interface ChatView: BaseView {
    fun navigateConversation(userVO: UserVO, currentUserVO: UserVO)
    fun renderChatContacts(users: List<Pair<UserVO, ConversationVO>>)
}