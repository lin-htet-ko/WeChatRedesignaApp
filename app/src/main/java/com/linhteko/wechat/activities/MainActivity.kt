package com.linhteko.wechat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.ContactAndMainDelegate
import com.linhteko.shared.mvp.presenters.MainPresenter
import com.linhteko.shared.mvp.views.MainView
import com.linhteko.shared.utils.Alerts
import com.linhteko.shared.utils.showToast
import com.linhteko.wechat.R
import com.linhteko.wechat.dialogs.EditProfileDialog
import com.linhteko.wechat.fragments.*
import com.linhteko.wechat.presenters.MainPresenterImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    companion object {
        const val IE_USER_ID = "user_id"
        fun newIntent(context: Context, userId: String? = null): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(IE_USER_ID, userId)
            return intent
        }
    }

    private lateinit var mainPresenter: MainPresenter
    private var userId: String? = null
    private var mUser: UserVO? = null
    private var editProfileDialog: EditProfileDialog? = null
    private var contactAndMainDelegate: ContactAndMainDelegate? = null
    private val scanner = registerForActivityResult(ScanContract()) {
        if (it.contents != null) {
            mainPresenter.addFriend(userId, it.contents)
        } else {
            showToast(Alerts.ALERT_ERR)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpPresenter()
        setUpToolbar()
        getUserId()
        setUpBottomNavWithFragments()
        mainPresenter.onUiReady(this)
    }

    private fun getUserId() {
        intent.getStringExtra(IE_USER_ID)?.let {
            userId = it
            mainPresenter.currentUser = userId

            mainPresenter.getProfile(it)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(tlMain)
    }

    private fun setUpBottomNavWithFragments() {
        if (userId != null)
            bnvMain.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_moment -> {
                        setUpToolbarContents(R.string.lbl_moment, R.drawable.ic_add_new_moment) {
                            startActivity(userId?.let { id ->
                                CreateNewMomentActivity.newIntent(
                                    this,
                                    id
                                )
                            })
                        }
                        if (userId != null)
                            replaceFragment(MomentFragment.newInstance(userId!!))
                    }
                    R.id.menu_chat -> {
                        setUpToolbarContents(R.string.lbl_chat, R.drawable.ic_search) {

                        }
                        replaceFragment(ChatFragment.newInstance(userId!!))
                    }
                    R.id.menu_contacts -> {
                        val fragment = ContactsFragment.newInstance(userId!!)
                        fragment.getDelegate {
                            contactAndMainDelegate = it
                        }
                        setUpToolbarContents(
                            R.string.lbl_contacts,
                            R.drawable.ic_add_new_contacts
                        ) {

                            openQrCodeScanner()

                        }
                        replaceFragment(fragment)
                    }
                    R.id.menu_profile -> {
                        setUpToolbarContents(R.string.lbl_me, R.drawable.ic_edit) {
                            editProfileDialog = mUser?.let { it1 ->
                                EditProfileDialog.newInstance(
                                    it1
                                )
                            }
                            editProfileDialog?.show(supportFragmentManager, "EditProfileDialog")
                        }
                        replaceFragment(ProfileFragment.newInstance(userId!!))
                    }
                    R.id.menu_setting -> {
                        val fragment = SettingFragment.newInstance()

                        setUpToolbarContents(R.string.lbl_setting, null) {

                        }
                        replaceFragment(fragment)
                    }
                }
                true
            }
        bnvMain.selectedItemId = R.id.menu_moment
    }

    private fun openQrCodeScanner() {
        val scanOption = ScanOptions()
        scanOption.setDesiredBarcodeFormats(ScanOptions.QR_CODE)

        scanner.launch(scanOption)
    }

    private fun setUpPresenter() {
        mainPresenter = ViewModelProvider(this)[MainPresenterImpl::class.java]
        mainPresenter.initView(this)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fcvMain, fragment).commit()
    }

    private fun setUpToolbarContents(
        @StringRes title: Int,
        @DrawableRes actionIcon: Int? = null,
        action: (String) -> Unit
    ) {
        tvToolbarTitle.text = getString(title)
        if (actionIcon != null) {
            ivToolbarAction.setImageResource(actionIcon)
        }
        ivToolbarAction.setOnClickListener {
            action.invoke(getString(title))
        }
    }

    override fun renderUser(userVO: UserVO) {
        mUser = userVO
        if (editProfileDialog?.showsDialog == true) {
            editProfileDialog?.dismiss()
        }
    }

    override fun addedFriend() {
        contactAndMainDelegate?.onAddedFriend()
    }

    override fun showError(message: String) {

    }

    override fun updateProfile(user: UserVO) {
        mainPresenter.updateProfile(user)
    }

    override fun navigateChat(userVO: UserVO) {
        if (mUser != null)
            startActivity(
                ChitchatActivity.newIntent(
                    this, userVO, mUser!!
                )
            )
    }

    override fun navigateChat(groupId: String) {
        if (mUser != null)
            startActivity(ChitchatActivity.newIntent(this, mUser!!, groupId))
    }

    override fun navigateToAddNewGroup() {
        startActivity(mainPresenter.currentUser?.let { AddNewGroupActivity.newIntent(this, it) })
    }
}
