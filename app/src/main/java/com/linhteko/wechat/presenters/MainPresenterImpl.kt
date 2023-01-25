package com.linhteko.wechat.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.AuthenticationModel
import com.linhteko.shared.data.model.FirebaseAuthenticationModelImpl
import com.linhteko.shared.data.model.MomentModel
import com.linhteko.shared.data.model.MomentModelImpl
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.MainPresenter
import com.linhteko.shared.mvp.views.MainView

class MainPresenterImpl : ViewModel(), MainPresenter {


    override lateinit var mView: MainView
    private val momentModel: MomentModel = MomentModelImpl
    private val authModel: AuthenticationModel = FirebaseAuthenticationModelImpl
    var user: MutableLiveData<UserVO> = MutableLiveData()

    override fun initView(view: MainView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {
        user.observe(owner) {
            mView.renderUser(it)
        }
    }

    override var currentUser: String? = null

    override fun getProfile(uid: String) {
        authModel.getUser(
            userId = uid,
            onSuccess = {
                if (it != null)
                    user.postValue(it)
            },
            onFailure = {
                mView.showError(it)
            }
        )
    }

    override fun updateProfile(userVO: UserVO) {
        authModel.updateProfile(userVO,
            onSuccess = {
                authModel.getUser(userVO.userId.toString(),
                    onSuccess = {
                        user.postValue(it)
                    },
                    onFailure = {
                        mView.showError(it)
                    })
            },
            onFailure = {
                mView.showError(it)
            })
    }

    override fun addFriend(me: String?, barcodeOwner: String) {
        authModel.addFriend(me, barcodeOwner,
            onSuccess = {
                        mView.addedFriend()
            },
            onFailure = {
                mView.showError(it)
            })
    }
}