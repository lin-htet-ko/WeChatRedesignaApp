package com.linhteko.shared.mvp.views

import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.base.BaseView

interface CreatePostView: BaseView {
    fun uploadedMoment()
    fun showUser(userVO: UserVO)
    fun pickImage()
    fun pickedImage(url: String)
    fun pickVideo(url: String)
}
