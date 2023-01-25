package com.linhteko.shared.mvp.presenters

import com.linhteko.shared.delegates.ActivePersonDelegate
import com.linhteko.shared.delegates.MessageDelegate
import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.ChatView

interface ChatPresenter: BasePresenter<ChatView>, ActivePersonDelegate, MessageDelegate {
    var userId: String?
}