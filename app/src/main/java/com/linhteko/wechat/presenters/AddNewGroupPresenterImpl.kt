package com.linhteko.wechat.presenters

import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.*
import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.AddNewGroupPresenter
import com.linhteko.shared.mvp.views.AddNewGroupView

class AddNewGroupPresenterImpl : ViewModel(), AddNewGroupPresenter {

    override var userId: String? = null
    private val contacts: MutableList<UserVO> = mutableListOf()
    private val members: MutableList<UserVO> = mutableListOf()

    override lateinit var mView: AddNewGroupView
    private val authModel: AuthenticationModel = FirebaseAuthenticationModelImpl
    private val storage: StorageModel = FirebaseStorageModelImpl
    private val chatModel: ChatModel = FirebaseChatModelImpl


    override fun initView(view: AddNewGroupView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {

    }

    override fun onTapContact(userVO: UserVO) {
//        userVO.isChecked = true

        val temp = contacts.map {
            val tempUser = it
            if (tempUser.userId == userVO.userId) {
                tempUser.isChecked = !tempUser.isChecked

                if (tempUser.isChecked)
                    members.add(userVO)
                else
                    members.remove(userVO)
            }
            tempUser
        }
        contacts.clear()
        contacts.addAll(temp)

        mView.renderContacts(contacts)
        mView.renderMembers(members)
    }

    override fun onDismissUser(userVO: UserVO) {
        members.remove(userVO)
        val tempContacts = contacts.map {
            if (it.userId == userVO.userId)
                it.isChecked = false

            it
        }
        contacts.clear()
        contacts.addAll(tempContacts)

        mView.renderContacts(contacts)

        mView.renderMembers(members)
    }

    override fun searchContacts(keyword: String) {
        if (keyword.isEmpty()) {
            mView.renderContacts(contacts)
        } else {
            val tempContacts = contacts.filter { it.name?.contains(keyword) == true }
            mView.renderContacts(tempContacts)
        }
    }

    override fun getContacts() {
        userId?.let {
            authModel.getContacts(it,
                onSuccess = { users ->
                    contacts.clear()
                    contacts.addAll(users)
                    mView.renderContacts(users)
                },
                onFailure = { err ->
                    mView.showError(err)
                })
        }
    }

    override fun createGroup(groupName: String, groupImage: Bitmap) {
        if (members.isEmpty()) {
            mView.showError("Add Members")
            return
        }
        val tempMembers: List<String> = mutableListOf(
            userId
        ).apply { addAll(members.map { it.userId }) }.filterNotNull()
        storage.uploadMedia(
            groupImage,
            onUploaded = {
                val group = GroupVO(
                    name = groupName,
                    image = it,
                    members = tempMembers
                )
                mView.showError("Please Wait")
                chatModel.createGroup(group, onSuccess = {
                    mView.navigateToBack()
                    mView.showError("Create Group Successfully")
                }, onFailure = { err ->
                    mView.showError(err)
                })
            },
            onFailure = {
                mView.showError(it)
            },
            root = "group-profile-images"
        )

    }
}