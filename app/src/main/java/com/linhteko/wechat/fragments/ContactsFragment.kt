package com.linhteko.wechat.fragments

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.linhteko.shared.data.vos.GroupVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.ContactAndMainDelegate
import com.linhteko.shared.delegates.MainAndFragmentCommunicateDelegate
import com.linhteko.shared.mvp.presenters.ContactsPresenter
import com.linhteko.shared.mvp.views.ContactsView
import com.linhteko.shared.utils.showToast
import com.linhteko.wechat.R
import com.linhteko.wechat.activities.GroupAdapter
import com.linhteko.wechat.adapters.ContactAdapter
import com.linhteko.wechat.presenters.ContactsPresenterImpl
import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : Fragment(), ContactsView {

    companion object {
        const val IE_USER_ID = "user-id-for-contact"
        fun newInstance(id: String) = ContactsFragment()
            .apply {
                arguments = Bundle().apply {
                    putString(IE_USER_ID, id)
                }
            }
    }

    private lateinit var viewModel: ContactsPresenter
    private var userId: String? = null
    private var mainDelegate: MainAndFragmentCommunicateDelegate? = null
    private var contactsAdapter: ContactAdapter? = null
    private var groupAdapter: GroupAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainDelegate = context as? MainAndFragmentCommunicateDelegate
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPresenter()
        getArgs()
        setUpRecyclerViews()
        setUpListener()
        viewModel.onUiReady(requireActivity())
    }

    private fun setUpListener() {
        tieContactsSearch.addTextChangedListener {
            viewModel.searchContacts(it.toString().trim())
        }
    }

    private fun setUpRecyclerViews() {
        contactsAdapter = ContactAdapter(contactDelegate = viewModel)
        rvContacts.adapter = contactsAdapter

        groupAdapter = GroupAdapter(groupAddDelegate = viewModel, groupDelegate = viewModel)
        rvContactGroup.adapter = groupAdapter
    }

    private fun getArgs() {
        userId = arguments?.getString(IE_USER_ID)
        viewModel.userId = userId
    }

    private fun setUpPresenter() {
        viewModel = ViewModelProvider(requireActivity())[ContactsPresenterImpl::class.java]
        viewModel.initView(this)
    }

    override fun renderContacts(userVOList: List<UserVO>) {
        contactsAdapter?.setNewData(userVOList)
    }

    override fun navigateToChat(userVO: UserVO) {
        mainDelegate?.navigateChat(userVO)
    }

    override fun navigateToChat(groupId: String) {
        mainDelegate?.navigateChat(groupId)
    }

    override fun navigateToAddNewGroup() {
        mainDelegate?.navigateToAddNewGroup()
    }

    override fun renderGroups(groupVOList: List<GroupVO>) {
        groupAdapter?.setNewData(groupVOList)
    }

    override fun showError(message: String) {
        requireContext().showToast(message)
    }

    override fun onAddedFriend() {
        viewModel.getContacts(userId)
    }

    fun getDelegate(delegate: (ContactAndMainDelegate)->Unit){
        delegate(this)
    }
}