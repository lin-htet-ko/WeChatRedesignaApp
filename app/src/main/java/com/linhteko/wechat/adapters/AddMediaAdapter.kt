package com.linhteko.wechat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.MediaVO
import com.linhteko.shared.data.vos.MediaVO.Companion.MEDIA_TYPE_ADD
import com.linhteko.shared.data.vos.MediaVO.Companion.MEDIA_TYPE_PHOTO
import com.linhteko.shared.delegates.AddMediaDelegate
import com.linhteko.wechat.R
import com.linhteko.wechat.viewholders.AddMediaViewHolder
import com.linhteko.wechat.viewholders.MediaPhotoViewHolder

class AddMediaAdapter(
    private val addMediaDelegate: AddMediaDelegate,
    private var items: List<MediaVO> = listOf<MediaVO>()
) : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val MEDIA_PHOTO = 2
        const val ADD_MEDIA = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return when (viewType) {
            ADD_MEDIA -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.viewholder_media_add, parent, false)
                AddMediaViewHolder(view, addMediaDelegate)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.viewholder_media_photo, parent, false)
                MediaPhotoViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is AddMediaViewHolder -> {
                holder.bindData()
            }
            is MediaPhotoViewHolder -> {
                if (items.isNotEmpty())
                    holder.bindData(items[position])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(list: List<MediaVO>) {
        items = list
        notifyDataSetChanged()
    }

    fun getItems(): List<MediaVO>{
        return items
    }

    override fun getItemViewType(position: Int): Int {
        if (items.isNotEmpty()) {
            val item = items[position]
            when (item.type) {
                MEDIA_TYPE_PHOTO -> {
                    return MEDIA_PHOTO
                }
                MEDIA_TYPE_ADD -> {
                    return ADD_MEDIA
                }
            }
        }
        return super.getItemViewType(position)
    }
}