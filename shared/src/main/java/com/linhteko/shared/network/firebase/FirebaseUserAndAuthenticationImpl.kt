package com.linhteko.shared.network.firebase

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.network.UserAndAuthentication
import com.linhteko.shared.utils.Alerts
import com.linhteko.shared.utils.FirebaseApiConstants.COLLECTION_USER
import com.linhteko.shared.utils.FirebaseApiConstants.SUB_COLLECTION_CONTACTS
import java.util.concurrent.TimeUnit

object FirebaseUserAndAuthenticationImpl : UserAndAuthentication {

    private val firestore: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth


    override fun createUserWithPhone(
        verificationId: String,
        smsCode: String,
        onSuccess: (userId: String?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, smsCode)
        signInWithPhoneAuthCredential(phoneAuthCredential, onSuccess, onFailure)
    }

    override fun getOtp(
        activity: Activity,
        phone: String,
        onMessageReceived: (verificationId: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                onFailure(p0.localizedMessage ?: "Something Wrong")
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                onMessageReceived(p0)
            }
        }

        val phoneAuthOption = PhoneAuthOptions.Builder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callback)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOption)
    }

    private fun signInWithPhoneAuthCredential(
        credential: AuthCredential,
        onSuccess: (userId: String?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnSuccessListener { onSuccess(it.user?.uid) }
            .addOnFailureListener { onFailure(it.localizedMessage ?: "Something Wrong!") }
    }

    override fun registerUser(
        userVO: UserVO,
        onSuccess: (Boolean) -> Unit,
        onFailure: (String) -> Unit
    ) {

        val id = userVO.userId

        if (id != null) {
            firestore.collection("users").document(id).set(userVO).addOnSuccessListener {
                onSuccess(true)
            }.addOnFailureListener {
                onFailure(it.localizedMessage ?: "Something Wrong")
            }
        } else {
            onFailure("Phone number must be added.")
        }
    }

    override fun login(
        phone: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firestore.collection("users")
            .whereEqualTo("phone", phone)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener {
                val docs = it.documents
                if (docs.size > 0) {
                    val user = docs[0].toObject(UserVO::class.java)
                    if (user?.phone == phone && user.password == password) {
                        user.userId?.let { id -> onSuccess(id) }
                    } else {
                        onFailure("Invalid Phone Number Or Password")
                    }
                }

            }.addOnFailureListener {
                onFailure(it.localizedMessage ?: "Something Wrong")
            }
    }


    override fun hasCurrentUser(user: (has: Boolean, userId: String?) -> Unit) {
        val current = auth.currentUser
        if (current != null) {
            user(true, current.uid)
        } else {
            user(false, null)
        }
    }

    override fun getUser(
        userId: String,
        onSuccess: (UserVO?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firestore.collection(COLLECTION_USER).document(userId).get()
            .addOnSuccessListener {
                onSuccess(it.toObject(UserVO::class.java))
            }.addOnFailureListener {
                onFailure(it.localizedMessage ?: "Something Wrong")
            }
    }

    override fun updateProfile(user: UserVO, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firestore.collection(COLLECTION_USER).document(user.userId.toString())
            .set(user)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { onFailure(it.localizedMessage ?: Alerts.ALERT_ERR) }
    }

    override fun addFriend(
        userId: String?,
        qrCodeOwner: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (userId != null) {
            getUser(qrCodeOwner,
                onSuccess = {
                    if (it != null) {
                        firestore.collection(COLLECTION_USER).document(userId)
                            .collection(SUB_COLLECTION_CONTACTS)
                            .document(qrCodeOwner)
                            .set(it)
                            .addOnSuccessListener {
                                getUser(userId,
                                    onSuccess = { user ->
                                        if (user != null) {
                                            firestore.collection(COLLECTION_USER).document(qrCodeOwner)
                                                .collection(SUB_COLLECTION_CONTACTS)
                                                .document(userId)
                                                .set(user)
                                                .addOnSuccessListener {
                                                    onSuccess()
                                                }.addOnFailureListener { err ->
                                                    onFailure(
                                                        err.localizedMessage ?: Alerts.ALERT_ERR
                                                    )
                                                }
                                        }
                                    },
                                    onFailure = { err ->
                                        onFailure(err)
                                    })
                            }
                            .addOnFailureListener { err ->
                                onFailure(err.localizedMessage ?: Alerts.ALERT_ERR)
                            }

                    }
                },
                onFailure = {
                    onFailure(it)
                }
            )

        }
    }

    override fun getContacts(
        userId: String?,
        onSuccess: (List<UserVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (userId != null) {
            firestore.collection(COLLECTION_USER).document(userId)
                .collection(SUB_COLLECTION_CONTACTS)
                .get()
                .addOnSuccessListener {
                    val docs = it.documents
                    onSuccess(docs.mapNotNull { doc -> doc.toObject(UserVO::class.java) })
                }
                .addOnFailureListener {
                    onFailure(it.localizedMessage ?: Alerts.ALERT_ERR)
                }
        }
    }
}