package com.linhteko.shared.delegates

import com.linhteko.shared.data.vos.UserVO

interface ProfileEditDelegate {

    fun updateProfile(user: UserVO)
}