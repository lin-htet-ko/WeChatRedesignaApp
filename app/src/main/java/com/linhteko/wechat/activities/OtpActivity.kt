package com.linhteko.wechat.activities

import `in`.aabhasjindal.otptextview.OTPListener
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.linhteko.shared.delegates.ColoredButtonDelegate
import com.linhteko.shared.mvp.presenters.OtpPresenter
import com.linhteko.shared.mvp.views.OtpView
import com.linhteko.shared.utils.showSnackBar
import com.linhteko.wechat.R
import com.linhteko.wechat.presenters.OtpPresenterImpl
import com.linhteko.wechat.viewpods.ColoredButtonViewPod
import kotlinx.android.synthetic.main.activity_otp.*
import kotlinx.android.synthetic.main.activity_otp.view.*

class OtpActivity : AppCompatActivity(), OtpView {

    companion object {
        fun newIntent(context: Context): Intent {
            val intent = Intent(context, OtpActivity::class.java)
            return intent
        }
    }

    private lateinit var mOtpPresenter: OtpPresenter
    private var phone: String = ""
    private var btnVerify: ColoredButtonViewPod? = null
    private var btnGetOtp: ColoredButtonViewPod? = null
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        setUpPresenter()
        setUpViewPods()
        setUpEditWatcher()
        validateOtp()
        mOtpPresenter.onUiReady(this)
    }

    private fun setUpEditWatcher() {
        tieOtpPhone.addTextChangedListener {
            phone = it.toString().trim()
            if (phone.isNotEmpty() && phone.length == 12) {
                btnGetOtp?.setEnable(true)
            } else {
                btnGetOtp?.setEnable(false)
            }
        }
    }

    private fun setUpViewPods() {
        btnGetOtp = (vpOtpGet as ColoredButtonViewPod)
        btnGetOtp?.apply {
            setEnable(false)
            setUpContent(getString(R.string.lbl_get_otp))
            setUpDelegate(object : ColoredButtonDelegate {
                override fun onTapColorButtonTap() {
                    validatePhone()
                }
            })
        }

        btnVerify = (vpBtnOtpVerify as ColoredButtonViewPod)
        btnVerify?.apply {
            setEnable(false)
            setUpContent(getString(R.string.lbl_verify))

            setUpDelegate(object : ColoredButtonDelegate {
                override fun onTapColorButtonTap() {
                    if (btnVerify?.btnEnable == true) {
                        val otp = otpOtpView?.otp
                        if (!otp.isNullOrEmpty())
                            verificationId?.let {
                                mOtpPresenter.onTapVerify(
                                    otp = otp,
                                    phone = phone,
                                    verificationId = it
                                )
                            }
                    }
                }
            })
        }

    }

    private fun validatePhone() {
        phone = tieOtpPhone.text.toString().trim()
        if (phone.isEmpty()) {
            tilOtpPhone.error = getString(R.string.lbl_required)
            return
        }
//        if (!phone.startsWith("959")) {
//            tilOtpPhone.error = "Phone number must start with 959"
//            return
//        }
        if (phone.length != 12) {
            tilOtpPhone.error = "Phone number should be 11 length"
            return
        }

        mOtpPresenter.onTapGetOtp(this, phone) {
            verificationId = it
        }
    }


    private fun validateOtp() {
        otpOtpView.otpListener = object : OTPListener {
            override fun onInteractionListener() {

            }

            override fun onOTPComplete(otp: String) {
                if (otp.isNotEmpty() && btnVerify?.btnEnable == true)
                    verificationId?.let {
                        mOtpPresenter.onTapVerify(
                            otp = otp,
                            phone = phone,
                            verificationId = it
                        )
                    }
            }
        }
    }


    private fun setUpPresenter() {
        mOtpPresenter = ViewModelProvider(this)[OtpPresenterImpl::class.java]
        mOtpPresenter.initView(this)
    }

    override fun navigateToSignUp(userId: String, phone: String) {
        startActivity(SignUpActivity.newIntent(this, userId, phone))
        finish()
    }

    override fun otpButtonEnable() {
        btnVerify?.setEnable(true)
    }

    override fun showError(message: String) {
        nsvOtpRoot.showSnackBar(message)
    }
}