package com.linhteko.wechat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.delegates.PostDelegate
import com.linhteko.wechat.R
import com.linhteko.wechat.viewholders.PostGeneralViewHolder
import com.linhteko.wechat.viewholders.PostVideoViewHolder

class PostAdapter(
    private var items: List<PostVO> = listOf<PostVO>(),
    private val postDelegate: PostDelegate,
    private val userId: String? = null
) : RecyclerView.Adapter<ViewHolder>() {
    companion object {
        const val POST_VIEW_IMAGE = 1
        const val POST_VIEW_VIDEO = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            POST_VIEW_VIDEO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_post_video, parent, false)
                PostVideoViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.viewholder_post_general, parent, false)
                PostGeneralViewHolder(view, postDelegate)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (items.isNotEmpty()){
            val item = items[position]
            when (holder) {
                is PostVideoViewHolder -> {
                    item.contentVideo?.videoLink?.let { holder.bindVideo(item) }
                }
                is PostGeneralViewHolder -> {
                    holder.bind(item)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(list: List<PostVO>) {
        items = list.reversed()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (items.isNotEmpty()) {
            val post = items[position]
            when (post.postType) {
                PostVO.POST_TYPE_VIDEO -> {
                    return POST_VIEW_VIDEO
                }
                PostVO.POST_TYPE_IMAGE -> {
                    return POST_VIEW_IMAGE
                }
            }
        }
        return super.getItemViewType(position)
    }
}