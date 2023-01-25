package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.linhteko.shared.data.vos.PostVO
import com.linhteko.wechat.adapters.PhotoAdapter
import com.linhteko.wechat.viewpods.PostHeaderViewPod
import kotlinx.android.synthetic.main.viewholder_post_general.view.*
import kotlinx.android.synthetic.main.viewholder_post_video.view.*

class PostVideoViewHolder(itemView: View) : ViewHolder(itemView) {

    fun bindVideo(postVO: PostVO){
        (itemView.vpPostVideoTextHeader as PostHeaderViewPod).apply {
            if (postVO.userImg != null && postVO.userName != null)
                setUpContents(postVO.userImg!!, postVO.userName!!, postVO.uploadTime, postVO.postId)
        }
        val player = ExoPlayer.Builder(itemView.context).build()
        val mediaItem = postVO.contentVideo?.videoLink?.let { MediaItem.fromUri(it) }

        itemView.vvMomentPostVideo.player = player
        if (mediaItem != null) {
            player.setMediaItem(mediaItem)
        }
        player.prepare()

        itemView.tvPostVideoTextDesc.text = postVO.description ?: ""
    }
}