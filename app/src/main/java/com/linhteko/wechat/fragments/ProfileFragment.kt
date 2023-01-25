package com.linhteko.wechat.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.linhteko.shared.data.vos.PostVO
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.mvp.presenters.ProfilePresenter
import com.linhteko.shared.mvp.views.ProfileView
import com.linhteko.shared.utils.*
import com.linhteko.wechat.R
import com.linhteko.wechat.adapters.PostAdapter
import com.linhteko.wechat.dialogs.QrCodeDialog
import com.linhteko.wechat.presenters.ProfilePresenterImpl
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ProfileView {

    companion object {
        const val ARG_USER_ID = "user-id-for-profile"
        fun newInstance(userId: String) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_USER_ID, userId)
            }
        }
    }

    private lateinit var viewModel: ProfilePresenter
    private var intentIntegrator: IntentIntegrator? = null
    private lateinit var bookmarkAdapter: PostAdapter
    private var profileUri : Uri? = null
    private lateinit var getContent: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvProfileBookmarkEmpty.show()
        tvProfileBookmarkedLabel.hide()

        getContent = registerForActivityResult(ActivityResultContracts.GetContent(), requireActivity().activityResultRegistry) {
            if (it != null) {
                profileUri = it
                generateBitmap(profileUri!!, requireActivity().contentResolver)?.let { bitmap ->
                    viewModel.changeProfileImage(bitmap)
                }

                ivProfileUpload.setImageURI(it)
            }
        }

        setUpPresenter()
        getUserId()
        setUpScanner()
        viewModel.onUiReady(this)
    }

    private fun setUpRecyclerView(userId: String) {
        bookmarkAdapter = PostAdapter(
            postDelegate = viewModel,
            userId = userId
        )
        rvProfileBookmarks.adapter = bookmarkAdapter
    }

    private fun setUpScanner() {
        intentIntegrator = IntentIntegrator(requireActivity())
    }

    private fun getUserId() {
        arguments?.getString(ARG_USER_ID)?.let {
            viewModel.userId = it

            viewModel.getUser(it)

            setUpRecyclerView(it)

            setUpListener()
        }
    }

    private fun setUpListener() {
        ivProfileUpload.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private fun setUpPresenter() {
        viewModel = ViewModelProvider(requireActivity())[ProfilePresenterImpl::class.java]
        viewModel.initView(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.userId?.let { viewModel.getUser(it) }
    }

    override fun renderUserUi(userVO: UserVO?) {
        userVO?.apply {
            tvProfileName.text = name ?: ""
            tvProfilePhone.text = phone ?: ""
            tvProfileBirthday.text = "$dayOfBirth-$monthOfBirth-$yearOfBirth"
            tvProfileGender.text = gender ?: ""

            profileImg?.let { ivProfileImage.loadNetworkImage(it) }

            val encoder = BarcodeEncoder()
            val bitmap = encoder.encodeBitmap(
                userId,
                BarcodeFormat.QR_CODE,
                resources.getDimension(R.dimen.size_small_qr).toInt(),
                resources.getDimension(R.dimen.size_small_qr).toInt()
            )
            ivProfileQrCode.setImageBitmap(bitmap)

            ivProfileQrCode.setOnClickListener {
                userId?.let { uid -> QrCodeDialog.newInstance(uid).show(childFragmentManager, "qrcode-dialog") }
            }
        }
    }

    override fun showBookmarks(momments: List<PostVO>) {
        if (momments.isEmpty()){
            tvProfileBookmarkEmpty.show()
            tvProfileBookmarkedLabel.hide()
        }else{
            tvProfileBookmarkEmpty.hide()
            tvProfileBookmarkedLabel.show()
        }
        bookmarkAdapter.setNewData(momments)
    }

    override fun uploadedProfile() {
        requireContext().showToast(getString(R.string.lbl_profile_image_success))
    }

    override fun showError(message: String) {
        llProfile.showSnackBar(message)
    }

}