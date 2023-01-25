package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.MessageDelegate
import com.linhteko.shared.utils.calculateUploadTime
import com.linhteko.shared.utils.getDays
import com.linhteko.shared.utils.loadNetworkImage
import kotlinx.android.synthetic.main.viewholder_message.view.*

class MessageViewHolder(itemView: View, private val messageDelegate: MessageDelegate) :
    ViewHolder(itemView) {

    fun bind(pair: Pair<UserVO, ConversationVO>) {
        val user = pair.first
        val conversationVO = pair.second
        itemView.ivMessagePersonImg.loadNetworkImage(user.profileImg ?: "")
        itemView.tvMessageUserName.text = user.name ?: ""
        itemView.tvMessageStatus.text =
            if (conversationVO.file != null &&
                conversationVO.type != ConversationVO.CONVERSATION_TYPE_TEXT.second
            ) {
                if (conversationVO.type == ConversationVO.CONVERSATION_TYPE_IMAGE.second) {
                    "Image"
                } else {
                    "Video"
                }
            } else {
                conversationVO.message ?: ""
            }
        itemView.tvMessageDuration.text = calculateUploadTime(conversationVO.id)

        itemView.setOnClickListener {
            messageDelegate.onTapMessage(user)
        }
    }
}