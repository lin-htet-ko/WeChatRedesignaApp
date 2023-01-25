package com.linhteko.wechat.viewholders

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.utils.calculateUploadTime
import com.linhteko.shared.utils.getWidthAndHeight
import kotlinx.android.synthetic.main.viewholder_chat_photo.view.*

class ConversationPhotoViewHolder(itemView: View, private val userId: String) :
    ViewHolder(itemView) {

    fun bind(conversationVO: ConversationVO) {

        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.gravity = if (conversationVO.userId == userId) Gravity.END else Gravity.START

        itemView.llChatPhoto.layoutParams = layoutParams

        itemView.tvChatPhotoTime.gravity =
            if (conversationVO.userId != userId) Gravity.END else Gravity.START

        itemView.context.getWidthAndHeight(conversationVO.file ?: "", size = { width, height ->


            if (height >= width) {
                itemView.ivChatPhoto.layoutParams.width = (width * 0.5).toInt()
                itemView.ivChatPhoto.layoutParams.height = (height * 0.4).toInt()
                itemView.ivChatPhoto.requestLayout()

            } else {
                itemView.ivChatPhoto.layoutParams.width = (width * 0.1).toInt()
                itemView.ivChatPhoto.layoutParams.height = (height * 0.15).toInt()
                itemView.ivChatPhoto.requestLayout()
            }




        }, bitmap = {
            itemView.ivChatPhoto.setImageBitmap(it)
        })
        itemView.tvChatPhotoTime.text = calculateUploadTime(conversationVO.id)

        itemView.setOnClickListener {

        }
    }
}