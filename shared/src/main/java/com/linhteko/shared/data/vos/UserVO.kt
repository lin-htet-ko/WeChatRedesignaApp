package com.linhteko.shared.data.vos

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserVO(
    val userId: String? = null,
    val name: String? = null,
    val phone: String? = null,
    val password: String? = null,
    val dayOfBirth: Int? = null,
    val monthOfBirth: String? = null,
    val yearOfBirth: Int? = null,
    val profileImg: String? = null,
    val gender: String? = null
): Parcelable{

    @IgnoredOnParcel
    var isChecked = false
}
