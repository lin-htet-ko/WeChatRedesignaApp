package com.linhteko.shared.mvp.presenters

import com.linhteko.shared.delegates.ContactDelegate
import com.linhteko.shared.delegates.GroupAddDelegate
import com.linhteko.shared.delegates.GroupDelegate
import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.ContactsView

interface ContactsPresenter: BasePresenter<ContactsView>, ContactDelegate, GroupAddDelegate, GroupDelegate {
    var userId: String?

    fun getContacts(userId: String?)

    fun getGroups(userId: String)
    fun searchContacts(keyword: String)
}