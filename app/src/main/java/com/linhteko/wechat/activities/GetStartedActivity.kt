package com.linhteko.wechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.linhteko.shared.mvp.presenters.GetStartedPresenter
import com.linhteko.shared.mvp.views.GetStartedView
import com.linhteko.wechat.R
import com.linhteko.wechat.presenters.GetStartedPresenterImpl
import com.linhteko.wechat.viewpods.ColoredButtonViewPod
import com.linhteko.wechat.viewpods.OutlinedButtonViewPod
import kotlinx.android.synthetic.main.activity_get_started.*

class GetStartedActivity : AppCompatActivity(), GetStartedView {

    private lateinit var getStartedPresenter : GetStartedPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        setUpPresenter()
        haveUser()
        setUpViewPods()
        getStartedPresenter.onUiReady(this)
    }

    private fun haveUser() {
        getStartedPresenter.checkHasUser()
    }

    private fun setUpViewPods() {
        (vpGetStartedLogin as ColoredButtonViewPod).apply {
            setUpContent(getString(R.string.lbl_login))
            setUpDelegate(this@GetStartedActivity)
        }
        (vpGetStartedSignUp as OutlinedButtonViewPod).apply {
            setUpContent(getString(R.string.lbl_sign_up))
            setUpDelegate(this@GetStartedActivity)
        }

    }

    private fun setUpPresenter() {
        getStartedPresenter = ViewModelProvider(this)[GetStartedPresenterImpl::class.java]
        getStartedPresenter.initView(this)
    }

    override fun navigateToSignUp() {
        startActivity(OtpActivity.newIntent(this))
    }

    override fun navigateToLogin() {
        startActivity(LoginActivity.newIntent(this))
    }

    override fun navigateToMainScreen(uid: String) {
        startActivity(MainActivity.newIntent(this,uid))
        finish()
    }


    override fun showError(message: String) {
        
    }

    override fun onTapColorButtonTap() {
        getStartedPresenter.onTapLogin()
    }

    override fun setOutlinedButtonOnClickListener() {
        getStartedPresenter.onTapSignUp()
    }
}