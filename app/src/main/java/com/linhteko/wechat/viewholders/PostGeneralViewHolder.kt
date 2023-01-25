package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.delegates.PostReactionDelegate
import com.linhteko.wechat.R
import com.linhteko.wechat.adapters.PhotoAdapter
import com.linhteko.wechat.viewpods.PostHeaderViewPod
import com.linhteko.wechat.viewpods.PostReactionsViewPod
import kotlinx.android.synthetic.main.viewholder_post_general.view.*

class PostGeneralViewHolder(
    itemView: View,
    private val postReactionDelegate: PostReactionDelegate,
) : ViewHolder(itemView) {

    fun bind(postVO: PostVO) {
        (itemView.vpPostTextHeader as PostHeaderViewPod).apply {
            if (postVO.userImg != null && postVO.userName != null)
                setUpContents(postVO.userImg!!, postVO.userName!!, postVO.uploadTime, postVO.postId)
        }

        itemView.tvPostTextDesc.text = postVO.description

        itemView.rvMomentPostPhotos.adapter = PhotoAdapter(postVO.contentImages)

        (itemView.vpPostTextReactions as PostReactionsViewPod).apply {
            setUpDelegate(postVO.postId, postReactionDelegate)

            val likes = postVO.likes
            var fav = false
            for (like in likes){
                if (like.userId == postVO.userId)
                {
                    fav = true
                    break
                }
            }
            setUpFavorite(
                if (fav) {
                    R.drawable.ic_baseline_favorite_24
                }
                else {
                    R.drawable.ic_baseline_favorite_border_24
                },
                postVO.likes.count()
            )

            val bookmarkUsers = postVO.bookmarks
            var bookmark = false
            for (user in bookmarkUsers){
                if (user == postVO.userId){
                    bookmark = true
                    break
                }
            }
            setBookMark(if (bookmark) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_outline)
        }

    }
}