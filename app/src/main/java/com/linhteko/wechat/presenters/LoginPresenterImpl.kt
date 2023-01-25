package com.linhteko.wechat.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.AuthenticationModel
import com.linhteko.shared.data.model.FirebaseAuthenticationModelImpl
import com.linhteko.shared.mvp.presenters.LoginPresenter
import com.linhteko.shared.mvp.views.LoginView

class LoginPresenterImpl: ViewModel(), LoginPresenter {


    override lateinit var mView: LoginView
    private val authModel: AuthenticationModel = FirebaseAuthenticationModelImpl

    override fun initView(view: LoginView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {
        
    }

    override fun onTapLogin(phone: String, password: String) {
        authModel.login(phone, password,
        onSuccess = {
            mView.navigateToMainScreen(it)
        },
        onFailure = {
            mView.showError(it)
        })

    }

    override fun onTapForgotPassword() {

    }
}