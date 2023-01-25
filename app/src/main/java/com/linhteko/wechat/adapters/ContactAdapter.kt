package com.linhteko.wechat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.ContactDelegate
import com.linhteko.wechat.R
import com.linhteko.wechat.viewholders.ContactViewHolder

class ContactAdapter(
    private var items: List<UserVO> = listOf<UserVO>(),
    private var contactDelegate: ContactDelegate
): RecyclerView.Adapter<ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_contact, parent, false)
        return ContactViewHolder(view, contactDelegate)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        if (items.isNotEmpty()){
            holder.bindData(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(list: List<UserVO>){
        items = list
        notifyDataSetChanged()
    }
}