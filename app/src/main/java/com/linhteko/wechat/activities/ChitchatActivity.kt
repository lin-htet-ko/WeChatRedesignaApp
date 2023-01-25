package com.linhteko.wechat.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.ConversationPresenter
import com.linhteko.shared.mvp.views.ConversationView
import com.linhteko.shared.utils.Alerts
import com.linhteko.shared.utils.generateBitmap
import com.linhteko.shared.utils.loadNetworkImage
import com.linhteko.wechat.R
import com.linhteko.wechat.adapters.ConversationAdapter
import com.linhteko.wechat.presenters.ConversationPresenterImpl
import kotlinx.android.synthetic.main.activity_chitchat.*

class ChitchatActivity : AppCompatActivity(), ConversationView {
    companion object {
        const val IE_USER = "user-object-for-chatting"
        const val IE_CURRENT_USER = "current-user"
        const val IE_GROUP_ID = "group-id-for-chat"
        fun newIntent(context: Context, userVO: UserVO, currentUser: UserVO): Intent {
            val intent = Intent(context, ChitchatActivity::class.java)
            intent.putExtra(IE_USER, userVO)
            intent.putExtra(IE_CURRENT_USER, currentUser)
            return intent
        }

        fun newIntent(context: Context, currentUser: UserVO, groupId: String): Intent {
            val intent = Intent(context, ChitchatActivity::class.java)
            intent.putExtra(IE_CURRENT_USER, currentUser)
            intent.putExtra(IE_GROUP_ID, groupId)
            return intent
        }
    }

    private lateinit var conversationAdapter: ConversationAdapter
    private lateinit var presenter: ConversationPresenter
    private var currentUser: UserVO? = null
    private var otherUser: UserVO? = null
    private var groupId: String? = null
    private val imageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null && currentUser != null && otherUser != null) {
            val bitmap = generateBitmap(it)
            if (bitmap != null) {
                presenter.sendImageMessage(bitmap, currentUser?.userId!!, otherUser!!)
            }
        }
        if (it != null && currentUser != null && groupId != null) {
            val bitmap = generateBitmap(it)
            if (bitmap != null) {
                presenter.sendImageGroupMessage(bitmap, currentUser?.userId!!, groupId!!)
            }
        }

    }
    private val videoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null && currentUser != null && otherUser != null) {
            presenter.sendVideoMessage(it, currentUser?.userId!!, otherUser!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chitchat)

        setUpPresenter()
        setUpActionbar()
        getUserObj()
        setUpConversationRecyclerView()
        setUpListener()
        presenter.onUiReady(this)
    }

    private fun setUpPresenter() {
        presenter = ViewModelProvider(this)[ConversationPresenterImpl::class.java]
        presenter.initView(this)
    }

    private fun setUpListener() {
        etChatTypeMessage.addTextChangedListener {
            val text = it.toString().trim()

            if (text.isEmpty()) {
                btnChitchatSend.setImageResource(R.drawable.ic_send_disable)
            } else {
                btnChitchatSend.setImageResource(R.drawable.ic_send)
            }
        }

        btnChitchatBack.setOnClickListener {
            super.onBackPressed()
        }
        btnSendCamera.setOnClickListener {
            launchCamera()
        }
        btnSendImage.setOnClickListener {
            imageLauncher.launch("image/*")
        }
    }

    private fun getUserObj() {
        currentUser = intent.getParcelableExtra<UserVO>(IE_CURRENT_USER)
        otherUser = intent.getParcelableExtra<UserVO>(IE_USER)
        groupId = intent.getStringExtra(IE_GROUP_ID)
        presenter.user = currentUser

        if (currentUser != null && otherUser != null) {
            otherUser?.apply {
                ivChitchatProfile.loadNetworkImage(profileImg ?: "")
                tvChitchatProfileName.text = name ?: Alerts.ALERT_ERR
                tvChitchatActiveStatus.text = "Online"

                if (otherUser?.userId != null) {
                    setUpListener(currentUser?.userId!!, otherUser!!)
                    presenter.getConversation(otherUser?.userId!!)
                }
            }
        }
        if (currentUser != null && groupId != null) {
            presenter.getGroup(groupId!!)
            presenter.getGroupConversations(groupId!!)
        }
    }

    private fun setUpConversationRecyclerView() {
        if (presenter.user != null) {
            conversationAdapter = ConversationAdapter(userId = presenter.user?.userId!!)
            rvChitchatConversation.adapter = conversationAdapter
        }
    }

    private fun setUpListener(uid: String, userVO: UserVO) {
        btnSendLocation.setOnClickListener { }
        btnSendVoice.setOnClickListener { }
        btnSendGif.setOnClickListener { }
        btnChitchatSend.setOnClickListener {
            if (currentUser != null && otherUser != null)
                validateAndSendMessage(uid, userVO)
        }


    }

    private fun launchCamera() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 101)
    }

    private fun validateAndSendMessage(uid: String, userVO: UserVO) {
        val text = etChatTypeMessage.text.toString().trim()

        if (text.isEmpty())
            return

        presenter.sendMessage(
            userVO.userId!!,
            ConversationVO(
                name = userVO.name,
                message = text,
                profilePic = userVO.profileImg,
                userId = uid
            )
        )

        etChatTypeMessage.text?.clear()
        btnChitchatSend.setImageResource(R.drawable.ic_send_disable)

    }

    private fun setUpActionbar() {
        setSupportActionBar(tbChitchat)
    }

    override fun renderConversations(conversations: List<ConversationVO>) {
        conversationAdapter.setNewData(conversations)
    }

    override fun renderGroupUI(groupVO: GroupVO) {
        ivChitchatProfile.loadNetworkImage(groupVO.image ?: "")
        tvChitchatProfileName.text = groupVO.name ?: Alerts.ALERT_ERR
        tvChitchatActiveStatus.text = "Online"

        conversationAdapter.setNewData(groupVO.messages.values.toList())

        btnChitchatSend.setOnClickListener {
            if (currentUser != null && groupId != null) {
                validateAndSendGroupMessage(groupId!!, currentUser!!)
            }
        }
    }

    override fun validateAndSendGroupMessage(groupId: String, currentUser: UserVO) {
        val text = etChatTypeMessage.text.toString().trim()

        if (text.isEmpty())
            return

        presenter.sendGroupMessage(
            groupId, ConversationVO(
                name = currentUser.name,
                message = text,
                profilePic = currentUser.profileImg,
                userId = currentUser.userId
            )
        )

        etChatTypeMessage.text?.clear()
        btnChitchatSend.setImageResource(R.drawable.ic_send_disable)


    }

    override fun showError(message: String) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            val bitmap = data.extras?.get("data") as? Bitmap

            if (bitmap != null && currentUser != null && otherUser != null) {
//                val bitmap = generateBitmap(uriForCameraLauncer)
                presenter.sendImageMessage(bitmap, currentUser?.userId!!, otherUser!!)
            }
            if (bitmap != null && currentUser != null && groupId != null){
                presenter.sendImageGroupMessage(bitmap, currentUser?.userId!!, groupId!!)
            }
        }
    }
}

