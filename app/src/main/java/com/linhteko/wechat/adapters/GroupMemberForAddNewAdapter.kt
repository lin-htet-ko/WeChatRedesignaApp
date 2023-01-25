package com.linhteko.wechat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.GroupMemberForAddNewDelegate
import com.linhteko.wechat.R
import com.linhteko.wechat.viewholders.GroupMemberForAddViewHolder

class GroupMemberForAddNewAdapter(
    private var items: List<UserVO> = listOf<UserVO>(),
    private val groupMemberForAddNewDelegate: GroupMemberForAddNewDelegate
): RecyclerView.Adapter<GroupMemberForAddViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberForAddViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_group_member_for_add_new, parent, false)
        return GroupMemberForAddViewHolder(view, groupMemberForAddNewDelegate)
    }

    override fun onBindViewHolder(holder: GroupMemberForAddViewHolder, position: Int) {
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