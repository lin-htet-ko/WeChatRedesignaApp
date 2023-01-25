package com.linhteko.wechat.presenters

import android.app.Activity
import android.util.Log.d
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.AuthenticationModel
import com.linhteko.shared.data.model.FirebaseAuthenticationModelImpl
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.OtpPresenter
import com.linhteko.shared.mvp.views.OtpView

class OtpPresenterImpl : ViewModel(), OtpPresenter {


    override lateinit var mView: OtpView
    private val authenticationModel: AuthenticationModel = FirebaseAuthenticationModelImpl

    override fun initView(view: OtpView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {

    }

    override fun onTapGetOtp(
        activity: Activity,
        phone: String,
        onMessageReceived: (String) -> Unit,
    ) {

        authenticationModel.getOtp(
            activity,
            phone,
            onMessageReceived = {
                d("OKOK Receive", it)
                onMessageReceived(it)
                mView.otpButtonEnable()
            },
            onFailure = {
                mView.showError(it)
            }
        )
    }

    override fun onTapVerify(verificationId: String, otp: String, phone: String) {

        fun register(userId: String){
            authenticationModel.register(
                UserVO(
                    userId = userId,
                    phone = phone
                ),
                onSuccess = {
                    mView.navigateToSignUp(userId, phone)
                },
                onFailure = {
                    mView.showError(it)
                }
            )
        }

        authenticationModel.createUserWithPhone(
            verificationId,
            otp,
            onSuccess = {
                if (it != null) {
                    register(it)
                }
                d("OKOK User", "it")
            },
            onFailure = {
                mView.showError(it)
            }
        )


    }

    override fun onTapResendCode(phone: String) {

    }

}