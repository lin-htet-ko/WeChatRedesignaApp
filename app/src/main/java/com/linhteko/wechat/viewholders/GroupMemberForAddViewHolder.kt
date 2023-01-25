package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.GroupMemberForAddNewDelegate
import com.linhteko.shared.utils.loadNetworkImage
import kotlinx.android.synthetic.main.viewholder_group_member_for_add_new.view.*

class GroupMemberForAddViewHolder(
    itemView: View,
    private val groupMemberForAddNewDelegate: GroupMemberForAddNewDelegate
) : ViewHolder(itemView) {

    fun bind(userVO: UserVO) {
        itemView.tvGroupNewMemberName.text = userVO.name ?: ""
        itemView.ivGroupNewMemberImage.loadNetworkImage(userVO.profileImg ?: "")
        itemView.btnAddNewGroupDismiss.setOnClickListener {
            groupMemberForAddNewDelegate.onDismissUser(userVO)
        }
    }
}