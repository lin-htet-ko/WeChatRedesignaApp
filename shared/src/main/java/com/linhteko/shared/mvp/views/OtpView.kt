package com.linhteko.shared.mvp.views

import com.linhteko.shared.mvp.base.BaseView

interface OtpView: BaseView {

    fun navigateToSignUp(userId: String,phone: String)
    fun otpButtonEnable()
}
