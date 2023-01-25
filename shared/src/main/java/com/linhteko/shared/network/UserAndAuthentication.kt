package com.linhteko.shared.network

import android.app.Activity
import com.linhteko.shared.data.vos.UserVO

interface UserAndAuthentication {

    fun getOtp(
        activity: Activity,
        phone: String,
        onMessageReceived: (String) -> Unit,
        onFailure: (String) -> Unit
    )

    fun createUserWithPhone(
        verificationId: String,
        smsCode: String,
        onSuccess: (userId: String?) -> Unit,
        onFailure: (String) -> Unit
    )

    fun registerUser(
        userVO: UserVO,
        onSuccess: (Boolean) -> Unit,
        onFailure: (String) -> Unit
    )

    fun login(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    )

    fun hasCurrentUser(user: (has: Boolean, userId: String?) -> Unit)
    fun getUser(userId: String, onSuccess: (UserVO?) -> Unit, onFailure: (String) -> Unit)
    fun updateProfile(user: UserVO, onSuccess: () -> Unit, onFailure: (String) -> Unit)

    fun addFriend(
        userId: String?,
        qrCodeOwner: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )

    fun getContacts(
        userId: String?,
        onSuccess: (List<UserVO>) -> Unit,
        onFailure: (String) -> Unit
    )
}