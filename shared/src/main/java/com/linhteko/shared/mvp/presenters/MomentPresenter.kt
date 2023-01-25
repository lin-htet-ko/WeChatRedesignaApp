package com.linhteko.shared.mvp.presenters

import com.linhteko.shared.delegates.PostDelegate
import com.linhteko.shared.mvp.base.BasePresenter
import com.linhteko.shared.mvp.views.MomentView

interface MomentPresenter: BasePresenter<MomentView>, PostDelegate {
    var userId: String?

    fun getPosts()
}