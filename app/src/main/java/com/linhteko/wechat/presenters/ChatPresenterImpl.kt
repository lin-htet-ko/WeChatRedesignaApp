package com.linhteko.wechat.presenters

import android.util.Log.d
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.AuthenticationModel
import com.linhteko.shared.data.model.ChatModel
import com.linhteko.shared.data.model.FirebaseAuthenticationModelImpl
import com.linhteko.shared.data.model.FirebaseChatModelImpl
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.ChatPresenter
import com.linhteko.shared.mvp.views.ChatView

class ChatPresenterImpl : ViewModel(), ChatPresenter {

    override lateinit var mView: ChatView
    override var userId: String? = null
    private val chatModel: ChatModel = FirebaseChatModelImpl
    private val authenticationModel: AuthenticationModel = FirebaseAuthenticationModelImpl

    override fun initView(view: ChatView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {
        val users: MutableList<Pair<UserVO, ConversationVO>> = mutableListOf()
        userId?.let { uid ->
            chatModel.getContactsAndMessages(uid,
                onSuccess = { ids ->
                    ids.forEach { otherUid ->
                        authenticationModel.getUser(
                            otherUid,
                            onSuccess = { userVO ->
                                if (userVO != null) {
                                    chatModel.getLastConversation(
                                        uid,
                                        otherUid,
                                        { conversation ->
                                            if (conversation != null)
                                                users.add(userVO to conversation)

                                            mView.renderChatContacts(users)

                                        },
                                        { err -> mView.showError(err) })

                                }
                            },
                            onFailure = { err -> mView.showError(err) })
                    }
                },
                onFailure = {
                    mView.showError(it)
                })
        }
    }

    override fun onTapPerson(userId: Long) {

    }

    override fun onTapMessage(userVO: UserVO) {
        if (userId != null) {
            authenticationModel.getUser(userId!!,
                onSuccess = {
                    if (it != null)
                        mView.navigateConversation(userVO, it)
                },
                onFailure = {
                    mView.showError(it)
                })

        }
    }
}