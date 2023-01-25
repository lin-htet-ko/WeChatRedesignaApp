package com.linhteko.wechat.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.linhteko.shared.data.vos.MediaVO
import com.linhteko.shared.utils.loadNetworkImage
import kotlinx.android.synthetic.main.viewholder_media_photo.view.*

class MediaPhotoViewHolder(itemView: View) : ViewHolder(itemView) {

    fun bindData(mediaVO: MediaVO) {
        itemView.ivMediaPhoto.loadNetworkImage(mediaVO.uri.toString())
    }
}