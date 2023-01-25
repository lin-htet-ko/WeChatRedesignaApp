package com.linhteko.wechat.viewpods

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.linhteko.shared.delegates.PostReactionDelegate
import kotlinx.android.synthetic.main.viewpod_post_reactions.view.*

class PostReactionsViewPod @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayoutCompat(context, attrs) {

    private var postReactionDelegate: PostReactionDelegate? = null

    fun setUpFavorite(
        @DrawableRes icon: Int,
        count: Int,
    ) {
        ivPostReactionFav.setImageResource(icon)
        tvPostReactionFav.text = count.toString()

    }

    fun setUpComment(
        @DrawableRes icon: Int,
        count: Int,
    ) {
        ivPostReactionFav.setImageResource(icon)
        tvPostReactionFav.text = count.toString()

    }

    fun setBookMark(
        @DrawableRes icon: Int,
    ) {
        ivPostReactionBookmark.setImageResource(icon)
    }

    fun setUpDelegate(postId: Long, delegate: PostReactionDelegate) {
        postReactionDelegate = delegate

        ivPostReactionBookmark.setOnClickListener { postReactionDelegate?.onTapBookmark(postId) }
        ivPostReactionComment.setOnClickListener { postReactionDelegate?.onTapComment(postId) }
        ivPostReactionFav.setOnClickListener { postReactionDelegate?.onTapFav(postId) }

    }
}