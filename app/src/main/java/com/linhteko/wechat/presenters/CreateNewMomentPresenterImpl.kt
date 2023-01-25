package com.linhteko.wechat.presenters

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.*
import com.linhteko.shared.data.vos.MediaVO
import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.mvp.presenters.CreatePostPresenter
import com.linhteko.shared.mvp.views.CreatePostView

class CreateNewMomentPresenterImpl : ViewModel(), CreatePostPresenter {

    override lateinit var mView: CreatePostView
    private val momentModel: MomentModel = MomentModelImpl
    private val authenticationModel: AuthenticationModel = FirebaseAuthenticationModelImpl
    private val storageModel: StorageModel = FirebaseStorageModelImpl

    override var userId: String? = null
    override var userProfileImg: String? = null
    override var userName: String? = null
    override var pickImgs: MutableList<MediaVO> = mutableListOf()
    override var pickVideos: MediaVO? = null

    override fun initView(view: CreatePostView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {
        pickImgs.add(MediaVO(type = MediaVO.MEDIA_TYPE_ADD))
    }

    override fun onTapAddMedia() {
        mView.pickImage()
    }

    override fun postMoment(postVO: PostVO) {
        momentModel.uploadMoment(
            postVO,
            {
                mView.uploadedMoment()
            },
            {
                mView.showError(it)
            }
        )
    }

    override fun getUser(userId: String) {
        authenticationModel.getUser(userId,
            onSuccess = {
                if (it != null)
                    mView.showUser(it)
            },
            onFailure = { err: String ->
                mView.showError(err)
            })
    }

    override fun uploadImg(image: Bitmap?) {
        if (image != null) {
            storageModel.uploadMedia(
                image,
                { mView.pickedImage(it) },
                { mView.showError(it) },
                "content-images"
            )
        }

    }

    override fun uploadVideo(uri: Uri) {
        storageModel.uploadMedia(
            uri = uri,
            {
                mView.pickVideo(it)
            },
            { mView.showError(it) },
            "content-videos"
        )
    }
}