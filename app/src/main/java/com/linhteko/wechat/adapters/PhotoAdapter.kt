package com.linhteko.wechat.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.linhteko.shared.data.vos.ImageVO
import com.linhteko.wechat.R
import com.linhteko.wechat.viewholders.PhotoViewHolder

class PhotoAdapter(
    private var items: List<ImageVO> = listOf<ImageVO>()
): RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  PhotoViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        if (items.isNotEmpty()){
            holder.bindData(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(list: List<ImageVO>){
        items = list
        notifyDataSetChanged()
    }
}