package com.linhteko.wechat.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.AuthenticationModel
import com.linhteko.shared.data.model.ChatModel
import com.linhteko.shared.data.model.FirebaseAuthenticationModelImpl
import com.linhteko.shared.data.model.FirebaseChatModelImpl
import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.ContactsPresenter
import com.linhteko.shared.mvp.views.ContactsView

class ContactsPresenterImpl : ViewModel(), ContactsPresenter {

    override lateinit var mView: ContactsView
    override var userId: String? = null
    private var groups: List<GroupVO> = listOf(
        GroupVO(type = GroupVO.TYPE_GROUP_ADD.second)
    )
    private var contacts: List<UserVO> = mutableListOf()
    private val authModel: AuthenticationModel = FirebaseAuthenticationModelImpl
    private val chatModel: ChatModel = FirebaseChatModelImpl

    override fun onUiReady(owner: LifecycleOwner) {
        userId?.let { getGroups(it) }
        getContacts(userId)
    }

    override fun onTapContact(userVO: UserVO) {
        mView.navigateToChat(userVO)
    }

    override fun onTapGroupAdd() {
        mView.navigateToAddNewGroup()
    }

    override fun onTapGroup(groupId: String) {
        mView.navigateToChat(groupId)
    }

    override fun initView(view: ContactsView) {
        mView = view
    }

    override fun getContacts(userId: String?) {
        authModel.getContacts(userId,
            onSuccess = {
                contacts = it
                mView.renderContacts(it)
            },
            onFailure = {
                mView.showError(it)
            })
    }

    override fun getGroups(userId: String) {
        chatModel.getGroups(userId,
            onSuccess = {
                val temp = it.toMutableList()
                temp.add(0, GroupVO(type = GroupVO.TYPE_GROUP_ADD.second))
                groups = temp
                mView.renderGroups(groups)
            },
            onFailure = {
                mView.showError(it)
            })
    }

    override fun searchContacts(keyword: String) {
        if (keyword.isEmpty()) {
            mView.renderContacts(contacts)
            return
        }
        val tempContacts = contacts.filter { it.name?.lowercase()?.contains(keyword.lowercase()) == true }
        mView.renderContacts(tempContacts)
    }


}