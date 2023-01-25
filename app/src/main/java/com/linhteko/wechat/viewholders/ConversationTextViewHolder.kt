package com.linhteko.wechat.viewholders

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout.LayoutParams
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.utils.calculateUploadTime
import com.linhteko.wechat.R
import kotlinx.android.synthetic.main.viewholder_chat_text.view.*

class ConversationTextViewHolder(itemView: View, private val userId: String) :
    ViewHolder(itemView) {

    fun bindData(conversationVO: ConversationVO) {
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = if (conversationVO.userId == userId) Gravity.END else Gravity.START
        itemView.llChatText.layoutParams = layoutParams

        if (conversationVO.userId == userId){
            itemView.tvChatText.setTextColor(itemView.context.getColor(R.color.white))
            itemView.tvChatTextTime.setTextColor(itemView.context.getColor(R.color.grey))

            itemView.llChatText.background = ResourcesCompat.getDrawable(itemView.context.resources, R.drawable.backgroud_chat_by_me, itemView.context.theme)
        }else{
            itemView.llChatText.background = ResourcesCompat.getDrawable(itemView.context.resources, R.drawable.backgroud_chat, itemView.context.theme)
            itemView.tvChatText.setTextColor(itemView.context.getColor(R.color.color_primary))
            itemView.tvChatTextTime.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
        }


        itemView.tvChatTextTime.gravity =
            if (conversationVO.userId != userId) Gravity.END else Gravity.START

        itemView.tvChatText.text = conversationVO.message
        itemView.tvChatTextTime.text = calculateUploadTime(conversationVO.id)
    }

}