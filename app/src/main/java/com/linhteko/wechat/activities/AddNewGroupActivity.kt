package com.linhteko.wechat.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.AddNewGroupPresenter
import com.linhteko.shared.mvp.views.AddNewGroupView
import com.linhteko.shared.utils.generateBitmap
import com.linhteko.shared.utils.showToast
import com.linhteko.wechat.R
import com.linhteko.wechat.adapters.ContactsForAddAdapter
import com.linhteko.wechat.adapters.GroupMemberForAddNewAdapter
import com.linhteko.wechat.presenters.AddNewGroupPresenterImpl
import kotlinx.android.synthetic.main.activity_add_new_group.*

class AddNewGroupActivity : AppCompatActivity(), AddNewGroupView {

    companion object {
        const val IE_USER_ID = "user-id-for-new-group"
        fun newIntent(context: Context, userId: String): Intent {
            val intent = Intent(context, AddNewGroupActivity::class.java)
            intent.putExtra(IE_USER_ID, userId)
            return intent
        }
    }

    private var groupMemberForAddNewAdapter: GroupMemberForAddNewAdapter? = null
    private var contactsForAddAdapter: ContactsForAddAdapter? = null
    private lateinit var presenter: AddNewGroupPresenter
    private var groupImage: Bitmap? = null
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            groupImage = generateBitmap(it)
            ivAddNewGroupImage.setImageBitmap(groupImage)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_group)

        setUpPresenter()
        getIntentData()
        setUpRecyclerViews()
        setUpListener()
        presenter.onUiReady(this)
    }

    private fun getIntentData() {
        intent.getStringExtra(IE_USER_ID)?.let {
            presenter.userId = it
            presenter.getContacts()
        }
    }

    private fun setUpListener() {
        ivCreateGroupClose.setOnClickListener {
            super.onBackPressed()
        }
        btnCreateGroups.setOnClickListener {
            validateValuesForCreateGroup()
        }
        ivAddNewGroupImage.setOnClickListener {
            getContent.launch("image/*")
        }

        etAddNewGroupName.addTextChangedListener {
            btnCreateGroups.isEnabled = it.toString().trim().isNotEmpty()
        }

        tieAddNewGroupContactName.addTextChangedListener {
            val keyword = it.toString().trim()
            searchContact(keyword)
        }
    }

    private fun validateValuesForCreateGroup() {
        val groupName = etAddNewGroupName.text.toString().trim()
        if (groupName.isEmpty()) {
            etAddNewGroupName.error = getString(R.string.lbl_required)
            return
        }
        if (groupImage == null){
            showToast(getString(R.string.lbl_require_group_image))
            return
        }
        presenter.createGroup(groupName = groupName, groupImage = groupImage!!)
    }

    private fun searchContact(keyword: String) {
        presenter.searchContacts(keyword)
    }

    private fun setUpPresenter() {
        presenter = ViewModelProvider(this)[AddNewGroupPresenterImpl::class.java]
        presenter.initView(this)
    }

    private fun setUpRecyclerViews() {
        groupMemberForAddNewAdapter =
            GroupMemberForAddNewAdapter(groupMemberForAddNewDelegate = presenter)
        rvAddNewGroupMembers.adapter = groupMemberForAddNewAdapter

        contactsForAddAdapter = ContactsForAddAdapter(contactForAddDelegate = presenter)
        rvAddNewGroupContacts.adapter = contactsForAddAdapter
    }

    override fun renderContacts(contacts: List<UserVO>) {
        contactsForAddAdapter?.setNewData(contacts)
    }

    override fun renderMembers(members: MutableList<UserVO>) {
        groupMemberForAddNewAdapter?.setNewData(members)
    }

    override fun navigateToBack() {
        super.onBackPressed()
    }

    override fun showError(message: String) {
        showToast(message)
    }
}