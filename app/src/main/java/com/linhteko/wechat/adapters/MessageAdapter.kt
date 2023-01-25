package com.linhteko.wechat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.MessageDelegate
import com.linhteko.wechat.R
import com.linhteko.wechat.viewholders.MessageViewHolder

class MessageAdapter(
    private var items: List<Pair<UserVO, ConversationVO> > = listOf<Pair<UserVO, ConversationVO>>(),
    private var messageDelegate: MessageDelegate
): RecyclerView.Adapter<MessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_message, parent, false)
        return MessageViewHolder(view, messageDelegate)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        if (items.isNotEmpty())
            holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(list: List<Pair<UserVO, ConversationVO>>){
        items = list
        notifyDataSetChanged()
    }
}