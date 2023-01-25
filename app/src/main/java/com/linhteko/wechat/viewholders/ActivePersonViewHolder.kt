package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.MessageDelegate
import com.linhteko.shared.utils.loadNetworkImage
import kotlinx.android.synthetic.main.viewholder_active_person.view.*
import kotlinx.android.synthetic.main.viewholder_message.view.*

class ActivePersonViewHolder(itemView: View, private val messageDelegate: MessageDelegate) : ViewHolder(itemView) {
    fun bind(userVO: UserVO) {
        itemView.ivActionPersonImg.loadNetworkImage(userVO.profileImg ?: "")
        itemView.tvActionPersonName.text = userVO.name ?: ""

        itemView.setOnClickListener {
            messageDelegate.onTapMessage(userVO)
        }
    }
}