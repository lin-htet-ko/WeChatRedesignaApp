package com.linhteko.wechat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.wechat.R
import com.linhteko.wechat.viewholders.ConversationPhotoViewHolder
import com.linhteko.wechat.viewholders.ConversationTextViewHolder
import com.linhteko.wechat.viewholders.ConversationVideoViewHolder

class ConversationAdapter(
    private var items: List<ConversationVO> = listOf<ConversationVO>(),
    private val userId: String
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ConversationVO.CONVERSATION_TYPE_TEXT.first -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_chat_text, parent, false)
                ConversationTextViewHolder(view, userId)
            }
            ConversationVO.CONVERSATION_TYPE_IMAGE.first -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_chat_photo, parent, false)
                ConversationPhotoViewHolder(view, userId)
            }
            ConversationVO.CONVERSATION_TYPE_VIDEO.first -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_chat_video, parent, false)
                ConversationVideoViewHolder(view, userId)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_chat_text, parent, false)
                ConversationTextViewHolder(view, userId)
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items.isNotEmpty())
            when (holder) {
                is ConversationTextViewHolder -> {
                    holder.bindData(items[position])
                }
                is ConversationPhotoViewHolder -> {
                    holder.bind(items[position])
                }
                is ConversationVideoViewHolder -> {
                    holder.bind(items[position])
                }
            }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(list: List<ConversationVO>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (items.isNotEmpty()) {
            val item = items[position]
            when (item.type) {
                ConversationVO.CONVERSATION_TYPE_TEXT.second -> {
                    return ConversationVO.CONVERSATION_TYPE_TEXT.first
                }
                ConversationVO.CONVERSATION_TYPE_IMAGE.second -> {
                    return ConversationVO.CONVERSATION_TYPE_IMAGE.first
                }
                ConversationVO.CONVERSATION_TYPE_VIDEO.second -> {
                    return ConversationVO.CONVERSATION_TYPE_VIDEO.first
                }

            }
        }
        return super.getItemViewType(position)
    }
}