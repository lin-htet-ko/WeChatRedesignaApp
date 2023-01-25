package com.linhteko.shared.delegates

import com.linhteko.shared.data.vos.UserVO

interface ContactForAddDelegate {

    fun onTapContact(userVO: UserVO)
}