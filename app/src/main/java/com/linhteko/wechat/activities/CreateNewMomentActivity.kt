package com.linhteko.wechat.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.linhteko.shared.data.vos.*
import com.linhteko.shared.mvp.presenters.CreatePostPresenter
import com.linhteko.shared.mvp.views.CreatePostView
import com.linhteko.shared.utils.generateBitmap
import com.linhteko.shared.utils.hide
import com.linhteko.shared.utils.loadNetworkImage
import com.linhteko.shared.utils.show
import com.linhteko.wechat.R
import com.linhteko.wechat.adapters.AddMediaAdapter
import com.linhteko.wechat.presenters.CreateNewMomentPresenterImpl
import kotlinx.android.synthetic.main.activity_create_new_moment.*

class CreateNewMomentActivity : AppCompatActivity(), CreatePostView {

    companion object {
        const val IE_USER_ID_NEW_MOMENT = "user-id-for-new-moment"
        fun newIntent(context: Context, uid: String): Intent {
            val intent = Intent(context, CreateNewMomentActivity::class.java)
            intent.putExtra(IE_USER_ID_NEW_MOMENT, uid)
            return intent
        }
    }

    private var mMediaAdapter: AddMediaAdapter? = null
    private lateinit var mPresenter: CreatePostPresenter
    private val picker =
        registerForActivityResult(ActivityResultContracts.GetContent(), activityResultRegistry) {
            if (it != null) {
                val type = contentResolver.getType(it)
                if (type?.isNotEmpty()== true && type.endsWith("mp4"))
                    mPresenter.uploadVideo(it)
                else if (
                    type?.endsWith("jpeg") == true ||
                    type?.endsWith("jpg") == true ||
                    type?.endsWith("png") == true
                )
                    mPresenter.uploadImg(generateBitmap(it))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_moment)

        setUpPresenter()
        setUpRecyclerViews()
        setUpListener()
        getUserId()
        mPresenter.onUiReady(this)
    }

    private fun getUserId() {
        intent.getStringExtra(IE_USER_ID_NEW_MOMENT)?.let {
            mPresenter.userId = it
            mPresenter.getUser(it)
        }
    }

    private fun setUpListener() {
        btnCreateMoments.setOnClickListener {
            validatePostFields()
        }
        ivCreateMomentClose.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun validatePostFields() {
        val desc = etCreateMomentDesc.text.toString().trim()
        if (desc.isEmpty()) {
            return
        }
        val postVO = PostVO(
            userId = mPresenter.userId,
            userImg = mPresenter.userProfileImg,
            userName = mPresenter.userName,
            description = desc,
            contentImages = mPresenter.pickImgs.mapNotNull {
                it.uri?.toString()?.let { uri -> ImageVO(uri) }
            },
            uploadTime = System.currentTimeMillis(),
            contentVideo = VideoVO(mPresenter.pickVideos?.uri.toString()),
            postType = if (mPresenter.pickVideos != null) PostVO.POST_TYPE_VIDEO else PostVO.POST_TYPE_IMAGE
        )

        mPresenter.postMoment(postVO)
    }

    private fun setUpRecyclerViews() {
        mMediaAdapter = AddMediaAdapter(
            addMediaDelegate = mPresenter,
            mPresenter.pickImgs
        )
        rvCreateMomentAddMedia.adapter = mMediaAdapter
    }

    private fun setUpPresenter() {
        mPresenter = ViewModelProvider(this)[CreateNewMomentPresenterImpl::class.java]
        mPresenter.initView(this)
    }

    override fun uploadedMoment() {
        finish()
    }

    override fun showUser(userVO: UserVO) {
        userVO.apply {
            profileImg?.let {
                mPresenter.userProfileImg = it
                ivCreateMomentProfile.loadNetworkImage(it)
            }
            name?.let {
                mPresenter.userName = it
                tvCreateMomentUserName.text = name
            }
        }
    }

    override fun pickImage() {
        picker.launch("*/*")
    }

    override fun pickedImage(url: String) {
        mPresenter.pickImgs.add(MediaVO(Uri.parse(url), MediaVO.MEDIA_TYPE_PHOTO))

        mMediaAdapter?.setNewData(mPresenter.pickImgs)
        vvCreateMoment.hide()
    }

    override fun pickVideo(url: String) {
        vvCreateMoment.show()
        val mediaItem = MediaItem.fromUri(url)
        val player = ExoPlayer.Builder(this).build()
        vvCreateMoment.player = player
        player.setMediaItem(mediaItem)
        player.prepare()

        mPresenter.pickVideos = MediaVO(Uri.parse(url), MediaVO.MEDIA_TYPE_VIDEO)

        mMediaAdapter?.setNewData(listOf(MediaVO(type = MediaVO.MEDIA_TYPE_ADD)))
    }

    override fun showError(message: String) {

    }
}