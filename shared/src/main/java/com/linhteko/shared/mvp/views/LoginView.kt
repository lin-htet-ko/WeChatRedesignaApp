package com.linhteko.shared.mvp.views

import com.linhteko.shared.delegates.ColoredButtonDelegate
import com.linhteko.shared.mvp.base.BaseView

interface LoginView: BaseView, ColoredButtonDelegate {

    fun navigateToMainScreen(userId: String)
}
