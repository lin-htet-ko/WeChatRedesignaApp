package com.linhteko.shared.mvp.presenters

import android.graphics.Bitmap
import com.linhteko.shared.delegates.ContactForAddDelegate
import com.linhteko.shared.delegates.GroupMemberForAddNewDelegate
import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.AddNewGroupView

interface AddNewGroupPresenter: BasePresenter<AddNewGroupView>, ContactForAddDelegate, GroupMemberForAddNewDelegate {
    var userId: String?
    fun searchContacts(keyword: String)
    fun getContacts()
    fun createGroup(groupName: String, groupImage: Bitmap)
}