package com.linhteko.wechat.presenters

import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.AuthenticationModel
import com.linhteko.shared.data.model.FirebaseAuthenticationModelImpl
import com.linhteko.shared.data.model.FirebaseStorageModelImpl
import com.linhteko.shared.data.model.StorageModel
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.RegisterPresenter
import com.linhteko.shared.mvp.views.RegisterView
import com.linhteko.shared.network.firebase.FirebaseStorageImpl

class RegisterPresenterImpl : ViewModel(), RegisterPresenter {

    private val authModel: AuthenticationModel = FirebaseAuthenticationModelImpl
    private val storageModel: StorageModel = FirebaseStorageModelImpl

    override fun registerUser(user: UserVO) {
        authModel.register(
            user,
            onSuccess = {
                mView.navigateToMainScreen(user)
            },
            onFailure = {
                mView.showError(it)
            }
        )
    }

    override fun uploadProfileImg(
        bitmap: Bitmap,
        onUploaded: (String) -> Unit,
    ) {
        storageModel.uploadMedia(bitmap, onUploaded, {
            mView.showError(it)
        })
    }

    override lateinit var mView: RegisterView

    override fun initView(view: RegisterView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {

    }
}