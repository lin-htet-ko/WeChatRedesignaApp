package com.linhteko.shared.mvp.presenters

import android.graphics.Bitmap
import android.net.Uri
import com.linhteko.shared.data.vos.MediaVO
import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.delegates.AddMediaDelegate
import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.CreatePostView

interface CreatePostPresenter : BasePresenter<CreatePostView>, AddMediaDelegate {
    var userId : String?
    var userProfileImg : String?
    var userName : String?
    var pickImgs: MutableList<MediaVO>
    var pickVideos: MediaVO?

    fun postMoment(postVO: PostVO)
    fun getUser(userId: String)
    fun uploadImg(image: Bitmap?)
    fun uploadVideo(uri: Uri)
}