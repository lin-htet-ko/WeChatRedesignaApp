package com.linhteko.shared.mvp.views

import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.base.BaseView

interface AddNewGroupView: BaseView {
    fun renderContacts(contacts: List<UserVO>)
    fun renderMembers(members: MutableList<UserVO>)
    fun navigateToBack()
}