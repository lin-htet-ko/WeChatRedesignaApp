package com.linhteko.shared.mvp.presenters

import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.LoginView

interface LoginPresenter: BasePresenter<LoginView> {

    fun onTapLogin(phone: String, password: String)
    fun onTapForgotPassword()

}
