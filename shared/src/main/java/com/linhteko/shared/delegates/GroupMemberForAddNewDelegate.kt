package com.linhteko.shared.delegates

import com.linhteko.shared.data.vos.UserVO

interface GroupMemberForAddNewDelegate {

    fun onDismissUser(userVO: UserVO)
}