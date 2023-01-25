package com.linhteko.wechat.viewholders

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.linhteko.shared.data.vos.ConversationVO
import kotlinx.android.synthetic.main.viewholder_chat_video.view.*

class ConversationVideoViewHolder(itemView: View, private val userId: String) :
    ViewHolder(itemView) {

    fun bind(conversationVO: ConversationVO) {
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = if (conversationVO.userId == userId) Gravity.END else Gravity.START
        itemView.llChatVideo.layoutParams = layoutParams

        itemView.tvChatVideoTime.gravity =
            if (conversationVO.userId != userId) Gravity.END else Gravity.START

        val mediaItem = MediaItem.fromUri(conversationVO.file ?: "")
        val player = ExoPlayer.Builder(itemView.context).build()
        player.setMediaItem(mediaItem)
        itemView.pvChatVideo.player = player
        player.prepare()

    }
}