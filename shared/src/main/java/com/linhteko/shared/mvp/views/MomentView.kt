package com.linhteko.shared.mvp.views

import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.delegates.PostDelegate
import com.linhteko.shared.mvp.base.BaseView

interface MomentView: BaseView {
    fun showMoments(moments: List<PostVO>)
}
