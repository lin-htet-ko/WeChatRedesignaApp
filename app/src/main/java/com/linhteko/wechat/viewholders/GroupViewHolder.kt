package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.delegates.GroupDelegate
import com.linhteko.shared.utils.loadNetworkImage
import kotlinx.android.synthetic.main.viewholder_group.view.*

class GroupViewHolder(itemView: View, private val delegate: GroupDelegate) : ViewHolder(itemView) {
    fun bind(groupVO: GroupVO) {
        itemView.ivGroupImage.loadNetworkImage(groupVO.image ?: "")
        itemView.tvGroupName.text = groupVO.name ?: ""

        itemView.setOnClickListener {
            delegate.onTapGroup(groupVO.id.toString())
        }
    }
}