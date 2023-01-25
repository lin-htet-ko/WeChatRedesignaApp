package com.linhteko.wechat.viewpods

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.linhteko.shared.delegates.PostHeaderDelegate
import com.linhteko.shared.utils.calculateUploadTime
import com.linhteko.shared.utils.loadNetworkImage
import kotlinx.android.synthetic.main.viewpod_post_header.view.*
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

class PostHeaderViewPod @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var mHeaderDelegate: PostHeaderDelegate? = null

    fun setUpDelegate(delegate: PostHeaderDelegate){
        mHeaderDelegate = delegate
    }

    fun setUpContents(
        userImg: String,
        userName: String,
        uploadTime: Long,
        postId: Long
    ) {
        ivPostHeaderProfile.loadNetworkImage(userImg)
        tvPostHeaderUserName.text = userName
        tvPostHeaderUploadTime.text = calculateUploadTime(uploadTime)

        ivPostHeaderMore.setOnClickListener {
            mHeaderDelegate?.onTapMoreOnPost(postId)
        }
    }

}