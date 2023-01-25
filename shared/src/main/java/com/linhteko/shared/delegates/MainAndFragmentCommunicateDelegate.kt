package com.linhteko.shared.delegates

import com.linhteko.shared.data.vos.UserVO

interface MainAndFragmentCommunicateDelegate{

    fun navigateChat(userVO: UserVO)
    fun navigateChat(groupId: String)
    fun navigateToAddNewGroup()
}