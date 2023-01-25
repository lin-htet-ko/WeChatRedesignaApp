package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.ImageVO
import com.linhteko.shared.utils.loadNetworkImage
import kotlinx.android.synthetic.main.viewholder_photo.view.*

class PhotoViewHolder(itemView: View) : ViewHolder(itemView) {


    init {
//        val img = itemView.ivVhPhoto
//        val width = img.drawable.intrinsicWidth
//        val height = img.drawable.intrinsicHeight
//
//        if (height>width){
//            img.layoutParams.width = width / 2
//            img.layoutParams.height = (width / 1.5).toInt()
//            img.requestLayout()
//        }
    }

    fun bindData(imageVO: ImageVO) {
        val img = itemView.ivVhPhoto
        img.loadNetworkImage(imageVO.imageLink)

    }
}

