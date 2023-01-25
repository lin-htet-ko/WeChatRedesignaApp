package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.delegates.GroupAddDelegate

class GroupAddViewHolder(itemView: View, private val groupAddDelegate: GroupAddDelegate) : ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            groupAddDelegate.onTapGroupAdd()
        }
    }
}