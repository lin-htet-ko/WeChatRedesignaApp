package com.linhteko.shared.mvp.views

import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.base.BaseView

interface ProfileView: BaseView {
    fun renderUserUi(userVO: UserVO?)
    fun showBookmarks(momments: List<PostVO>)
    fun uploadedProfile()
}