package com.linhteko.wechat.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.linhteko.shared.data.vos.ConversationVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.ChatPresenter
import com.linhteko.shared.mvp.views.ChatView
import com.linhteko.shared.utils.hide
import com.linhteko.shared.utils.show
import com.linhteko.wechat.R
import com.linhteko.wechat.activities.ChitchatActivity
import com.linhteko.wechat.adapters.ActivePersonAdapter
import com.linhteko.wechat.adapters.MessageAdapter
import com.linhteko.wechat.presenters.ChatPresenterImpl
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment(), ChatView {

    companion object {
        const val BE_USER_ID = "user-id-for-chat"
        fun newInstance(uid: String) = ChatFragment().apply {
            arguments = Bundle().apply {
                putString(BE_USER_ID, uid)
            }
        }
    }

    private lateinit var viewModel: ChatPresenter
    private var mActivePersonAdapter: ActivePersonAdapter? = null
    private var messageAdapter: MessageAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvChatEmptyHistory.show()
        tvChatActiveNowLabel.show()
        setUpPresenter()
        getUserId()
        setUpRecyclerView()
        viewModel.onUiReady(requireActivity())
    }

    private fun getUserId() {
        arguments?.getString(BE_USER_ID)?.let {
            viewModel.userId = it
        }
    }

    private fun setUpRecyclerView() {
        mActivePersonAdapter = ActivePersonAdapter(messageDelegate = viewModel)
        messageAdapter = MessageAdapter(messageDelegate = viewModel)

        rvChatsActivePersons.adapter = mActivePersonAdapter
        rvChatsMessage.adapter = messageAdapter
    }

    private fun setUpPresenter() {
        viewModel = ViewModelProvider(requireActivity())[ChatPresenterImpl::class.java]
        viewModel.initView(this)
    }

    override fun navigateConversation(userVO: UserVO, currentUserVO: UserVO) {
        startActivity(ChitchatActivity.newIntent(requireContext(), userVO, currentUserVO))
    }

    override fun renderChatContacts(users: List<Pair<UserVO, ConversationVO>>) {
        if (users.isEmpty()) {
            tvChatEmptyHistory.show()
            tvChatActiveNowLabel.show()
        }
        else {
            tvChatEmptyHistory.hide()
            tvChatActiveNowLabel.hide()
        }

        messageAdapter?.setNewData(users)
        mActivePersonAdapter?.setNewData(users.map { it.first })
    }

    override fun showError(message: String) {

    }

}