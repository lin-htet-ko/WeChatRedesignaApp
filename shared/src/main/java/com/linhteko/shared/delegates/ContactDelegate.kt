package com.linhteko.shared.delegates

import com.linhteko.shared.data.vos.UserVO

interface ContactDelegate {
    fun onTapContact(userVO: UserVO)
}