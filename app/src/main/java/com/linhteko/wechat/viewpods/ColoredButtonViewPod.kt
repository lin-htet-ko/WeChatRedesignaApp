package com.linhteko.wechat.viewpods

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.linhteko.shared.delegates.ColoredButtonDelegate
import com.linhteko.wechat.R
import kotlinx.android.synthetic.main.viewpod_colored_button.view.*

class ColoredButtonViewPod @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var mButtonDelegate: ColoredButtonDelegate? = null
    var btnEnable: Boolean = true

    fun setUpContent(text: String){
        tvColoredBtn.text = text
    }

    fun setUpDelegate(buttonDelegate: ColoredButtonDelegate){
        mButtonDelegate = buttonDelegate

        setOnClickListener {
            mButtonDelegate?.onTapColorButtonTap()
        }
    }

    fun setEnable(status: Boolean){
        btnEnable = status
        if (status){
            setBackgroundResource(R.drawable.background_colored_button)
        }else{
            setBackgroundResource(R.drawable.background_colored_button_disable)
        }
    }

}