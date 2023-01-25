package com.linhteko.shared.mvp.presenters

import android.app.Activity
import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.OtpView

interface OtpPresenter : BasePresenter<OtpView> {

    fun onTapGetOtp(activity: Activity, phone: String, onMessageReceived: (String) -> Unit)
    fun onTapVerify(verificationId: String, otp: String, phone: String)
    fun onTapResendCode(phone: String)
}