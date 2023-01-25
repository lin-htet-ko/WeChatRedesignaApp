package com.linhteko.wechat.activities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.data.vos.GroupVO.Companion.TYPE_GROUP_ADD
import com.linhteko.shared.data.vos.GroupVO.Companion.TYPE_GROUP_PEOPLE
import com.linhteko.shared.delegates.GroupAddDelegate
import com.linhteko.shared.delegates.GroupDelegate
import com.linhteko.wechat.R
import com.linhteko.wechat.viewholders.GroupAddViewHolder
import com.linhteko.wechat.viewholders.GroupViewHolder

class GroupAdapter(
    private var items: List<GroupVO> = listOf<GroupVO>(),
    private val groupAddDelegate: GroupAddDelegate,
    private val groupDelegate: GroupDelegate
) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_GROUP_PEOPLE.first -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_group, parent, false)
                GroupViewHolder(view, groupDelegate)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_new_group_add, parent, false)
                GroupAddViewHolder(view, groupAddDelegate)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items.isNotEmpty()){
            when (holder) {
                is GroupViewHolder -> {
                    holder.bind(items[position])
                }
                is GroupAddViewHolder -> {}
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(list: List<GroupVO>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (items.isNotEmpty()) {
            val item = items[position]
            when (item.type) {
                TYPE_GROUP_PEOPLE.second -> {
                    return TYPE_GROUP_PEOPLE.first
                }
                TYPE_GROUP_ADD.second -> {
                    return TYPE_GROUP_ADD.first
                }
            }
        }
        return super.getItemViewType(position)
    }
}