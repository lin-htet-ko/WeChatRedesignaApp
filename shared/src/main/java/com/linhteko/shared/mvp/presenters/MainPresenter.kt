package com.linhteko.shared.mvp.presenters

import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.MainView

interface MainPresenter: BasePresenter<MainView> {
    var currentUser: String?
    fun getProfile(uid: String)
    fun updateProfile(user: UserVO)
    fun addFriend(me: String?, barcodeOwner: String)
}
