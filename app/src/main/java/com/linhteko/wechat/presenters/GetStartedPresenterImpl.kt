package com.linhteko.wechat.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.AuthenticationModel
import com.linhteko.shared.data.model.FirebaseAuthenticationModelImpl
import com.linhteko.shared.mvp.presenters.GetStartedPresenter
import com.linhteko.shared.mvp.views.GetStartedView

class GetStartedPresenterImpl: GetStartedPresenter, ViewModel() {

    override lateinit var mView: GetStartedView
    private val authModel: AuthenticationModel = FirebaseAuthenticationModelImpl

    override fun initView(view: GetStartedView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {
        
    }

    override fun onTapSignUp() {
        mView.navigateToSignUp()
    }

    override fun onTapLogin() {
        mView.navigateToLogin()
    }

    override fun checkHasUser() {
        authModel.hasCurrentUser{has, userId ->
            if (has && userId != null) {
                mView.navigateToMainScreen(userId)
            }
        }
    }
}