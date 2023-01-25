package com.linhteko.shared.network.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.network.ApiService
import com.linhteko.shared.network.UserAndAuthentication
import com.linhteko.shared.utils.Alerts.ALERT_ERR
import com.linhteko.shared.utils.FirebaseApiConstants.COLLECTION_POST
import com.linhteko.shared.utils.FirebaseApiConstants.COLLECTION_USER
import com.linhteko.shared.utils.FirebaseApiConstants.SUB_COLLECTION_BOOKMARK
import com.linhteko.shared.utils.FirebaseApiConstants.SUB_COLLECTION_LIKE

object FirebaseApiService : ApiService {

    private val firestore = Firebase.firestore
    private val auth: UserAndAuthentication = FirebaseUserAndAuthenticationImpl

    override fun uploadMoment(moment: PostVO, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firestore.collection(COLLECTION_POST)
            .document(System.currentTimeMillis().toString())
            .set(moment)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.localizedMessage ?: ALERT_ERR) }
    }

    override fun getPost(onSuccess: (List<PostVO>) -> Unit, onFailure: (String) -> Unit) {
        firestore.collection(COLLECTION_POST).get()
            .addOnSuccessListener {
                val temp = mutableListOf<PostVO>()
                val tempForBookmark = mutableListOf<PostVO>()

                val posts = it.documents.mapNotNull { doc -> doc.toObject(PostVO::class.java) }
                posts.forEach { post ->
                    var postVO = post
                    firestore.collection(COLLECTION_POST)
                        .document(post.postId.toString())
                        .collection(SUB_COLLECTION_LIKE)
                        .get()
                        .continueWithTask { taskForLike ->
                            taskForLike.addOnSuccessListener { likes ->

                                postVO = postVO.copy(likes = likes.map { like ->
                                    like.toObject(
                                        UserVO::class.java
                                    )
                                })
                            }.addOnFailureListener { err ->
                                onFailure(err.localizedMessage ?: ALERT_ERR)
                            }

                            firestore.collection(COLLECTION_POST)
                                .document(post.postId.toString())
                                .collection(SUB_COLLECTION_BOOKMARK)
                                .get()
                        }.addOnSuccessListener { users ->

                            postVO = postVO.copy(bookmarks = users.mapNotNull { uid ->
                                uid.getString(
                                    "user_id"
                                )
                            })
                            temp.add(postVO)
                            onSuccess(temp)
                        }
                        .addOnFailureListener { err ->
                            onFailure(err.localizedMessage ?: ALERT_ERR)
                        }
                }

            }.addOnFailureListener {
                onFailure(it.localizedMessage ?: ALERT_ERR)
            }


    }

    override fun reactFav(
        postId: Long,
        userId: String?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        if (userId != null) {
            auth.getUser(userId,
                onSuccess = { vo ->
                    if (vo != null) {
                        firestore.collection(COLLECTION_POST)
                            .document(postId.toString())
                            .collection(SUB_COLLECTION_LIKE)
                            .document(userId).set(vo)
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { err ->
                                onFailure(err.localizedMessage ?: ALERT_ERR)
                            }
                    }
                },
                onFailure = {
                    onFailure(it)
                })
        }
    }

    override fun removeFav(
        postId: Long,
        userId: String?,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (userId != null) {
            auth.getUser(userId,
                onSuccess = { vo ->
                    if (vo != null) {
                        firestore.collection(COLLECTION_POST)
                            .document(postId.toString())
                            .collection(SUB_COLLECTION_LIKE)
                            .document(userId).delete()
                            .addOnSuccessListener {
                                onSuccess()
                            }
                            .addOnFailureListener { err ->
                                onFailure(err.localizedMessage ?: ALERT_ERR)
                            }
                    }
                },
                onFailure = {
                    onFailure(it)
                })
        }
    }

    override fun getMomentsBookmarkByUser(
        userId: String,
        onSuccess: (List<PostVO>) -> Unit,
        onFailure: (String) -> Unit
    ) {

        firestore.collection(COLLECTION_USER)
            .document(userId)
            .collection(SUB_COLLECTION_BOOKMARK)
            .get()
            .addOnSuccessListener {
                val tempMoments = mutableListOf<PostVO?>()
                val docs = it.documents
                for (doc in docs) {
                    tempMoments.add(doc.toObject(PostVO::class.java))
                }
                onSuccess(tempMoments.filterNotNull())
            }
            .addOnFailureListener { onFailure(it.localizedMessage ?: ALERT_ERR) }
    }

    override fun bookmarkByUser(
        userId: String,
        postVO: PostVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        firestore.collection(COLLECTION_POST)
            .document(postVO.postId.toString())
            .collection(SUB_COLLECTION_BOOKMARK)
            .document(userId)
            .set(mapOf("user_id" to userId))
            .continueWithTask {
                firestore.collection(COLLECTION_USER)
                    .document(userId)
                    .collection(SUB_COLLECTION_BOOKMARK)
                    .document(postVO.postId.toString())
                    .set(postVO)
            }
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure(it.localizedMessage ?: ALERT_ERR)
            }

    }

    override fun removeBookmarkByUser(
        userId: String,
        postVO: PostVO,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        firestore.collection(COLLECTION_POST)
            .document(postVO.postId.toString())
            .collection(SUB_COLLECTION_BOOKMARK)
            .document(userId)
            .delete()
            .continueWithTask {
                firestore.collection(COLLECTION_USER)
                    .document(userId)
                    .collection(SUB_COLLECTION_BOOKMARK)
                    .document(postVO.postId.toString())
                    .delete()
            }
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.localizedMessage ?: ALERT_ERR) }
    }
}