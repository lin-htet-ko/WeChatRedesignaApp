package com.linhteko.wechat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.ContactForAddDelegate
import com.linhteko.wechat.R
import com.linhteko.wechat.viewholders.ContactForAddGroupViewHolder

class ContactsForAddAdapter(
    private var items: List<UserVO> = listOf<UserVO>(),
    private val contactForAddDelegate: ContactForAddDelegate
): RecyclerView.Adapter<ContactForAddGroupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactForAddGroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_contact_for_add_new_group, parent, false)
        return ContactForAddGroupViewHolder(view, contactForAddDelegate)
    }

    override fun onBindViewHolder(holder: ContactForAddGroupViewHolder, position: Int) {
        if (items.isNotEmpty()){
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(list: List<UserVO>){
        items = list
        notifyDataSetChanged()
    }
}