package com.linhteko.shared.mvp.presenters

import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.GetStartedView

interface GetStartedPresenter: BasePresenter<GetStartedView> {

    fun onTapSignUp()
    fun onTapLogin()
    fun checkHasUser()
}