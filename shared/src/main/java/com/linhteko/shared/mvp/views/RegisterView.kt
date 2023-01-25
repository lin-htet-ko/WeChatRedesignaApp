package com.linhteko.shared.mvp.views

import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.base.BaseView

interface RegisterView: BaseView {
    fun navigateToMainScreen(user: UserVO)
}
