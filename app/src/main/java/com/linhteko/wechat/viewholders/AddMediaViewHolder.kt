package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.delegates.AddMediaDelegate

class AddMediaViewHolder(itemView: View, private val addMediaDelegate: AddMediaDelegate) :
    ViewHolder(itemView) {

        fun bindData(){
            itemView.setOnClickListener { addMediaDelegate.onTapAddMedia() }
        }
}