package com.linhteko.wechat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.linhteko.shared.mvp.presenters.LoginPresenter
import com.linhteko.shared.mvp.views.LoginView
import com.linhteko.shared.utils.showSnackBar
import com.linhteko.wechat.R
import com.linhteko.wechat.presenters.LoginPresenterImpl
import com.linhteko.wechat.viewpods.ColoredButtonViewPod
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            return intent
        }
    }

    private lateinit var mLoginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setUpPresenter()
        setUpListener()
        tiePhone.setText( "+66957274973")
        tiePassword.setText( "Password")
        mLoginPresenter.onUiReady(this)
    }

    private fun setUpListener() {
        (vpBtnLogin as ColoredButtonViewPod).apply {
            setUpContent(getString(R.string.lbl_login))
            setUpDelegate(this@LoginActivity)
        }
    }

    private fun setUpPresenter() {
        mLoginPresenter = ViewModelProvider(this)[LoginPresenterImpl::class.java]
        mLoginPresenter.initView(this)
    }

    override fun navigateToMainScreen(userId: String) {
        finishAffinity()
        startActivity(MainActivity.newIntent(this, userId))
    }

    override fun showError(message: String) {
        nsvLoginRoot.showSnackBar(message)
    }

    override fun onTapColorButtonTap() {
        val phone = tiePhone.text.toString().trim()
        val password = tiePassword.text.toString().trim()

        if (phone.isEmpty()) {
            tilLoginPhone.error = getString(R.string.lbl_required)
            return
        }

        if (password.isEmpty()){
            tilLoginPassword.error = getString(R.string.lbl_required)
            return
        }

        mLoginPresenter.onTapLogin(phone, password)
    }
}