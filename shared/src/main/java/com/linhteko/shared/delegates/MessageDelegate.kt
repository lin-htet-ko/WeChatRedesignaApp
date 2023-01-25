package com.linhteko.shared.delegates

import com.linhteko.shared.data.vos.UserVO

interface MessageDelegate {
    fun onTapMessage(userVO: UserVO)
}