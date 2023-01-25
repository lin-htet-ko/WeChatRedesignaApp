package com.linhteko.shared.data.model

import android.app.Activity
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.network.UserAndAuthentication
import com.linhteko.shared.network.firebase.FirebaseUserAndAuthenticationImpl

object FirebaseAuthenticationModelImpl : AuthenticationModel {

    private val mAuth: UserAndAuthentication = FirebaseUserAndAuthenticationImpl

    override fun register(
        userVO: UserVO,
        onSuccess: (Boolean) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuth.registerUser(userVO, onSuccess, onFailure)
    }

    override fun login(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuth.login(phone, password, onSuccess, onFailure)
    }

    override fun getOtp(
        activity: Activity,
        phone: String,
        onMessageReceived: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuth.getOtp(activity, phone, onMessageReceived, onFailure)
    }

    override fun createUserWithPhone(
        verificationId: String,
        smsCode: String,
        onSuccess: (userId: String?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuth.createUserWithPhone(verificationId, smsCode, onSuccess, onFailure)
    }

    override fun hasCurrentUser(user: (has: Boolean, userId: String?) -> Unit) {
        mAuth.hasCurrentUser(user)
    }

    override fun getUser(userId: String, onSuccess: (UserVO?) -> Unit, onFailure: (String) -> Unit) {
        mAuth.getUser(userId, onSuccess, onFailure)
    }

    override fun updateProfile(user: UserVO, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        mAuth.updateProfile(user, onSuccess, onFailure)
    }

    override fun addFriend(
        userId: String?,
        qrCodeOwner: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuth.addFriend(userId, qrCodeOwner, onSuccess, onFailure)
    }

    override fun getContacts(
        userId: String?,
        onSuccess: (List<UserVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        mAuth.getContacts(userId, onSuccess, onFailure)
    }

}