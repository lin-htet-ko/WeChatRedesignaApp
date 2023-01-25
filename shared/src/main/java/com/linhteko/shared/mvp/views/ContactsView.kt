package com.linhteko.shared.mvp.views

import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.ContactAndMainDelegate
import com.linhteko.shared.mvp.base.BaseView

interface ContactsView: BaseView, ContactAndMainDelegate{
    fun renderContacts(userVOList: List<UserVO>)
    fun navigateToChat(userVO: UserVO)
    fun navigateToChat(groupId: String)
    fun navigateToAddNewGroup()
    fun renderGroups(groupVOList: List<GroupVO>)
}