package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.ContactDelegate
import com.linhteko.shared.utils.Alerts
import com.linhteko.shared.utils.loadNetworkImage
import kotlinx.android.synthetic.main.viewholder_contact.view.*

class ContactViewHolder(itemView: View, private val contactDelegate: ContactDelegate) :
    ViewHolder(itemView) {
    fun bindData(userVO: UserVO) {
        itemView.ivContactPersonImg.loadNetworkImage(userVO.profileImg ?: "")
        itemView.tvContactName.text = userVO.name ?: Alerts.ALERT_ERR

        itemView.setOnClickListener {
            contactDelegate.onTapContact(userVO)
        }
    }
}