package com.linhteko.shared.mvp.presenters

import android.graphics.Bitmap
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.RegisterView

interface RegisterPresenter : BasePresenter<RegisterView> {
    fun registerUser(user: UserVO)
    fun uploadProfileImg(bitmap: Bitmap, onUploaded: (String) -> Unit)
}