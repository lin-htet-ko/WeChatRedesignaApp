package com.linhteko.shared.mvp.views

import com.linhteko.shared.delegates.ColoredButtonDelegate
import com.linhteko.shared.delegates.OutlinedButtonDelegate
import com.linhteko.shared.mvp.base.BaseView

interface GetStartedView: BaseView, ColoredButtonDelegate, OutlinedButtonDelegate {
    fun navigateToSignUp()
    fun navigateToLogin()
    fun navigateToMainScreen(uid: String)
}