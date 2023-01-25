package com.linhteko.wechat.viewholders

import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.ContactForAddDelegate
import com.linhteko.shared.utils.Alerts
import com.linhteko.shared.utils.loadNetworkImage
import com.linhteko.wechat.R
import kotlinx.android.synthetic.main.viewholder_contact_for_add_new_group.view.*

class ContactForAddGroupViewHolder(
    itemView: View,
    private val contactForAddDelegate: ContactForAddDelegate
) : ViewHolder(itemView) {

    fun bind(userVO: UserVO) {
        itemView.ivGroupNewMemberPersonImg.loadNetworkImage(userVO.profileImg ?: "")
        itemView.tvGroupNewMemberName.text = userVO.name ?: Alerts.ALERT_ERR
        if (userVO.isChecked) {
            itemView.ivContactForAddNewGroup.apply {
                background = ResourcesCompat.getDrawable(
                    itemView.resources,
                    R.drawable.background_circle_primary,
                    itemView.context.theme
                )
            }
        } else {
            itemView.ivContactForAddNewGroup.apply {
                background = ResourcesCompat.getDrawable(
                    itemView.resources,
                    R.drawable.background_circle_primary_stroke,
                    itemView.context.theme
                )
            }
        }

        itemView.setOnClickListener { contactForAddDelegate.onTapContact(userVO) }
    }
}