package com.linhteko.wechat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.MessageDelegate
import com.linhteko.wechat.R
import com.linhteko.wechat.viewholders.ActivePersonViewHolder

class ActivePersonAdapter(
    private var items: List<UserVO> = listOf<UserVO>(),
    private val messageDelegate: MessageDelegate
): RecyclerView.Adapter<ActivePersonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivePersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_active_person, parent, false)
        return ActivePersonViewHolder(view, messageDelegate)
    }

    override fun onBindViewHolder(holder: ActivePersonViewHolder, position: Int) {
        if (items.isNotEmpty())
            holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(list: List<UserVO>){
        items = list
        notifyDataSetChanged()
    }
}