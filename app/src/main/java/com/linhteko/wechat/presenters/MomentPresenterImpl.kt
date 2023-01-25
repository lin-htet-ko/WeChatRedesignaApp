package com.linhteko.wechat.presenters

import android.util.Log.d
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.linhteko.shared.data.model.MomentModel
import com.linhteko.shared.data.model.MomentModelImpl
import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.mvp.presenters.MomentPresenter
import com.linhteko.shared.mvp.views.MomentView

class MomentPresenterImpl : ViewModel(), MomentPresenter {

    override lateinit var mView: MomentView
    override var userId: String? = null
    private val momentModel: MomentModel = MomentModelImpl
    private val moments: MutableLiveData<List<PostVO>> = MutableLiveData()

    override fun initView(view: MomentView) {
        mView = view
    }

    override fun onUiReady(owner: LifecycleOwner) {
        getPosts()

        moments.observe(owner) { t -> mView.showMoments(t) }
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
                    getPosts()
                }, {
                    mView.showError(it)
                })
            } else {
                momentModel.reactFavourite(postId, userId, {
                    getPosts()
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
                if (uid == userId) {
                    bookmark = true
                    break
                }
            }

            if (!bookmark) {
                momentModel.bookmarkByUser(userId!!, post, {
                    getPosts()
                }, {
                    mView.showError(it)
                })
            } else {
                momentModel.removeBookmarkByUser(userId!!, post, {
                    getPosts()
                }, {
                    getPosts()
                })
            }

        }
    }

    override fun getPosts() {
        momentModel.getMoments({
            moments.postValue(it)
        }, {
            mView.showError(it)
        })
    }
}