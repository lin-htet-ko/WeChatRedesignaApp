package com.linhteko.wechat.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.ColoredButtonDelegate
import com.linhteko.shared.mvp.presenters.RegisterPresenter
import com.linhteko.shared.mvp.views.RegisterView
import com.linhteko.shared.utils.generateBitmap
import com.linhteko.shared.utils.getDays
import com.linhteko.shared.utils.showSnackBar
import com.linhteko.wechat.R
import com.linhteko.wechat.presenters.RegisterPresenterImpl
import com.linhteko.wechat.viewpods.ColoredButtonViewPod
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.text.DateFormatSymbols
import java.util.*

class SignUpActivity : AppCompatActivity(), RegisterView {

    companion object {
        private const val IE_SIGN_UP_PHONE = "phone-number"
        private const val IE_SIGN_UP_USER_ID = "user-id"
        fun newIntent(context: Context, userId: String, phone: String): Intent {
            val intent = Intent(context, SignUpActivity::class.java)
            intent.putExtra(IE_SIGN_UP_PHONE, phone)
            intent.putExtra(IE_SIGN_UP_USER_ID, userId)
            return intent
        }
    }

    private lateinit var mSignUpPresenter: RegisterPresenter
    private var gender: String? = null
    private var currentMonth: Int = 0
    private var currentDay = 0
    private var currentYear = 0
    private var profileUri: Uri? = null
    private var years: List<Int> = listOf()
    private var months: List<String> = listOf()
    private var days: List<Int> = listOf()
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent(), activityResultRegistry) {
            if (it != null) {
                profileUri = it
                ivSignUpProfile.setImageURI(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setUpPresenter()
        setUpSpinnerData()
        setUpViewPods()
        setUpListener()
        mSignUpPresenter.onUiReady(this)
    }

    private fun setUpViewPods() {
        btnSignUpBack.setOnClickListener { super.onBackPressed() }
        (vpSignUpBtn as ColoredButtonViewPod).apply {
            setUpContent(getString(R.string.lbl_sign_up))
            setUpDelegate(object : ColoredButtonDelegate {
                override fun onTapColorButtonTap() {
                    validateDataAndSignUp()
                }
            })
        }
    }

    private fun validateDataAndSignUp() {
        val name = tieSignUpName.text.toString().trim()
        val password = tieSignUpPassword.text.toString().trim()

        if (name.isEmpty()) {
            tieSignUpName.error = getString(R.string.lbl_required)
            return
        }
        if (password.isEmpty()) {
            tieSignUpPassword.error = getString(R.string.lbl_required)
            return
        }

        val phone = intent.getStringExtra(IE_SIGN_UP_PHONE)
        val userId = intent.getStringExtra(IE_SIGN_UP_USER_ID)


        if (phone != null && userId != null) {
            if (profileUri != null) {
                generateBitmap(profileUri!!, this.contentResolver)?.let {
                    mSignUpPresenter.uploadProfileImg(
                        it,
                        onUploaded = { profile ->
                            val user = UserVO(
                                userId = userId,
                                name = name,
                                phone = phone,
                                password = password,
                                dayOfBirth = currentDay,
                                monthOfBirth = months[currentMonth],
                                yearOfBirth = currentYear,
                                gender = gender,
                                profileImg = profile
                            )
                            mSignUpPresenter.registerUser(user)
                        }
                    )
                }
            }

        }
    }


    private fun setUpListener() {
        rgSignUpGender.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbSignUpMale -> {
                    gender = rbSignUpMale.text.toString()
                }
                R.id.rbSignUpFemale -> {
                    gender = rbSignUpFemale.text.toString()
                }
                R.id.rbSignUpOther -> {
                    gender = rbSignUpOther.text.toString()
                }
            }
        }

        ivSignUpProfile.setOnClickListener {
            getProfileImage()
        }
    }

    private fun getProfileImage() {
        getContent.launch("image/*")
    }

    private fun setUpSpinnerData() {
        val calendar = Calendar.getInstance()
        calendar.time = Date(Date().time)

        years = (1900..(calendar.get(Calendar.YEAR))).toList()
        months = DateFormatSymbols().months.toList()
        val monthsIndex = List(months.size) { index -> index }

        currentMonth = calendar.get(Calendar.MONTH)
        currentDay = calendar.get(Calendar.DAY_OF_MONTH) - 1
        currentYear = years.size - 1

        days = getDays(currentMonth, currentYear)

        spSignUpYear.adapter = ArrayAdapter(this, R.layout.spinner_item_view, years)
        spSignUpMonth.adapter = ArrayAdapter(this, R.layout.spinner_item_view, months)
        spSignUpDay.adapter = ArrayAdapter(this, R.layout.spinner_item_view, days)

        spSignUpYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.selectedItemPosition?.let { position ->
                    currentYear = years[position]
                    if (currentMonth == calendar.get(Calendar.MONTH) && currentYear == calendar.get(
                            Calendar.YEAR
                        )
                    ) {
                        currentDay = calendar.get(Calendar.DAY_OF_MONTH) - 1
                        spSignUpDay.setSelection(currentDay, true)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        spSignUpMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.selectedItemPosition?.let { position ->
                    currentMonth = monthsIndex[position]
                    days = getDays(currentMonth, currentYear)
                    setUpDays(days)
                    if (monthsIndex[position] == calendar.get(Calendar.MONTH) && currentYear == calendar.get(
                            Calendar.YEAR
                        )
                    ) {
                        currentDay = calendar.get(Calendar.DAY_OF_MONTH) - 1
                        spSignUpDay.setSelection(currentDay, true)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        spSignUpDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.selectedItemPosition?.let { position ->
                    currentDay = days[position]

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        spSignUpYear.setSelection(currentYear, true)
        spSignUpMonth.setSelection(currentMonth, true)
        spSignUpDay.setSelection(currentDay, true)
    }

    private fun setUpDays(days: List<Int>) {
        spSignUpDay.adapter = ArrayAdapter(this, R.layout.spinner_item_view, days)
    }


    private fun setUpPresenter() {
        mSignUpPresenter = ViewModelProvider(this)[RegisterPresenterImpl::class.java]
        mSignUpPresenter.initView(this)
    }

    override fun navigateToMainScreen(user: UserVO) {
        startActivity(MainActivity.newIntent(this, user.userId))
        finish()
    }

    override fun showError(message: String) {
        clSignUpRoot.showSnackBar(message)
    }
}