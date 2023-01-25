package com.linhteko.shared.mvp.base

import androidx.lifecycle.LifecycleOwner

interface BasePresenter<V: BaseView> {
    var mView : V
    fun initView(view: V)
    fun onUiReady(owner: LifecycleOwner)
}