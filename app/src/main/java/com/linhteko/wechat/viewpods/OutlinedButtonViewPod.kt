package com.linhteko.wechat.viewpods

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.linhteko.shared.delegates.OutlinedButtonDelegate
import kotlinx.android.synthetic.main.viewpod_outlined_button.view.*

class OutlinedButtonViewPod @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var mButtonDelegate: OutlinedButtonDelegate? = null

    fun setUpContent(text: String) {
        tvOutlinedBtn.text = text
    }

    fun setUpDelegate(buttonDelegate: OutlinedButtonDelegate) {
        mButtonDelegate = buttonDelegate

        setOnClickListener {
            mButtonDelegate?.setOutlinedButtonOnClickListener()
        }
    }
}