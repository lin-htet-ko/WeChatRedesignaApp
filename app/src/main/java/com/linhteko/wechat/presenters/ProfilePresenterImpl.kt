package com.linhteko.wechat.presenters

import android.graphics.Bitmap
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.*
import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.ProfilePresenter
import com.linhteko.shared.mvp.views.ProfileView

class ProfilePresenterImpl : ViewModel(), ProfilePresenter {

    override var userId: String? = null
    private val authModel: AuthenticationModel = FirebaseAuthenticationModelImpl
    private val momentModel: MomentModel = MomentModelImpl
    private val storageModel: StorageModel = FirebaseStorageModelImpl
    private val moments: MutableLiveData<List<PostVO>> = MutableLiveData()
    var user: UserVO? = null


    override fun getUser(userId: String) {
        authModel.getUser(userId, {
            user = it
            mView.renderUserUi(it)
        }, {
            mView.showError(it)
        })
        getBookmarks(userId)
    }

    override fun changeProfileImage(bitmap: Bitmap) {
        storageModel.uploadMedia(bitmap,
            onUploaded = {
                if (user != null)
                    authModel.updateProfile(
                        user!!.copy(profileImg = it),
                        onSuccess = {
                            mView.uploadedProfile()
                            if (user!!.userId != null)
                                getUser(user!!.userId!!)
                        },
                        onFailure = { err -> mView.showError(err) })
            },
            onFailure = {
                mView.showError(it)
            })
    }

    private fun getBookmarks(userId: String?) {
        if (userId != null)
            momentModel.getMomentsBookmarkByUser(userId, {
                moments.postValue(it)
            }, {
                mView.showError(it)
            })
    }

    override lateinit var mView: ProfileView

    override fun initView(view: ProfileView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {
        moments.observe(owner) { t -> mView.showBookmarks(t) }
    }

    override fun onTapContent(postId: Int) {

    }

    override fun onTapMoreOnPost(postId: Long) {

    }

    override fun onTapFav(postId: Long) {
        val post = moments.value?.first { it.postId == postId }
        val likes = post?.likes
        var fav = false
        if (likes != null) {
            for (user in likes) {
                if (user.userId == userId) {
                    fav = true
                    break
                }
            }
            if (fav) {
                momentModel.removeFavourite(postId, userId, {
                    getBookmarks(userId)
                }, {
                    mView.showError(it)
                })
            } else {
                momentModel.reactFavourite(postId, userId, {
                    getBookmarks(userId)
                }, {
                    mView.showError(it)
                })
            }
        }
    }

    override fun onTapComment(postId: Long) {

    }

    override fun onTapBookmark(postId: Long) {
        val post = moments.value?.first { it.postId == postId }
        var bookmark = false
        if (post != null && userId != null) {
            val bookmarks = post.bookmarks
            for (uid in bookmarks) {
                bookmark = true
                break
            }

            if (bookmark) {
                momentModel.bookmarkByUser(userId!!, post, {
                    getBookmarks(userId)
                }, {
                    mView.showError(it)
                })
            } else {
                momentModel.removeBookmarkByUser(userId!!, post, {
                    getBookmarks(userId)
                }, {
                    mView.showError(it)
                })
            }

        }
    }
}