package com.linhteko.wechat.presenters

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.ChatModel
import com.linhteko.shared.data.model.FirebaseChatModelImpl
import com.linhteko.shared.data.model.FirebaseStorageModelImpl
import com.linhteko.shared.data.model.StorageModel
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.ConversationPresenter
import com.linhteko.shared.mvp.views.ConversationView
import java.io.ByteArrayOutputStream

class ConversationPresenterImpl : ViewModel(), ConversationPresenter {

    override lateinit var mView: ConversationView
    override var user: UserVO? = null
    private val chatModel: ChatModel = FirebaseChatModelImpl
    private val storage: StorageModel = FirebaseStorageModelImpl


    override fun initView(view: ConversationView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {

    }

    override fun getConversation(uid: String) {
        if (user?.userId != null)
            chatModel.getConversations(
                user?.userId!!, uid,
                onSuccess = {
                    mView.renderConversations(it)
                },
                onFailure = {
                    mView.showError(it)
                })
    }

    override fun sendMessage(uid: String, message: ConversationVO) {
        if (user?.userId != null)
            chatModel.sendMessage(
                currentUser = user!!.userId!!,
                otherUser = uid,
                message = message,
                onSuccess = {},
                onFailure = {
                    mView.showError(it)
                }
            )
    }

    override fun sendImageMessage(bitmap: Bitmap, uid: String, otherUserVO: UserVO) {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)

        storage.uploadMedia(
            bitmap,
            onUploaded = {
                sendMessage(
                    otherUserVO.userId!!, ConversationVO(
                        name = user?.name,
                        profilePic = user?.profileImg,
                        userId = uid,
                        file = it,
                        type = ConversationVO.CONVERSATION_TYPE_IMAGE.second
                    )
                )
            },
            onFailure = {
                mView.showError(it)
            },
            root = "message-images"
        )
    }

    override fun sendVideoMessage(uri: Uri, userId: String, otherUser: UserVO) {
        storage.uploadMedia(
            uri,
            onUploaded = {
                sendMessage(
                    otherUser.userId!!, ConversationVO(
                        name = user?.name,
                        profilePic = user?.profileImg,
                        userId = userId,
                        file = it,
                        type = ConversationVO.CONVERSATION_TYPE_VIDEO.second
                    )
                )
            },
            onFailure = {
                mView.showError(it)
            }, "messages-videos"
        )
    }

    override fun getGroup(groupId: String) {
        chatModel.getGroup(
            groupId,
            onSuccess = {
                if (it != null)
                    mView.renderGroupUI(it)
            },
            onFailure = {
                mView.showError(it)
            }
        )
    }

    override fun sendGroupMessage(groupId: String, conversationVO: ConversationVO) {
        chatModel.sendGroupMessage(groupId, conversationVO,
            onSuccess = {

            },
            onFailure = {
                mView.showError(it)
            })
    }

    override fun getGroupConversations(groupId: String) {
        chatModel.getGroupConversations(groupId,
            onSuccess = {
                mView.renderConversations(it)
            },
            onFailure = {
                mView.showError(it)
            })
    }

    override fun sendImageGroupMessage(bitmap: Bitmap, userId: String, groupId: String) {
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)

        storage.uploadMedia(
            bitmap,
            onUploaded = {
                sendGroupMessage(
                    groupId, ConversationVO(
                        name = user?.name,
                        profilePic = user?.profileImg,
                        userId = userId,
                        file = it,
                        type = ConversationVO.CONVERSATION_TYPE_IMAGE.second
                    )
                )
            },
            onFailure = {
                mView.showError(it)
            },
            root = "group-message-images"
        )
    }
}