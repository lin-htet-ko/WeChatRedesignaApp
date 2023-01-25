package com.linhteko.shared.mvp.views

import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.MainAndFragmentCommunicateDelegate
import com.linhteko.shared.delegates.ProfileEditDelegate
import com.linhteko.shared.mvp.base.BaseView

interface MainView: BaseView, ProfileEditDelegate, MainAndFragmentCommunicateDelegate {
    fun renderUser(userVO: UserVO)
    fun addedFriend()
}