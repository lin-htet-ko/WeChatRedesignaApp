package com.linhteko.shared.mvp.presenters

import android.graphics.Bitmap
import com.linhteko.shared.delegates.PostDelegate
import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.ProfileView

interface ProfilePresenter: BasePresenter<ProfileView>, PostDelegate {
    var userId: String?
    fun getUser(userId: String)
    fun changeProfileImage(bitmap: Bitmap)
}