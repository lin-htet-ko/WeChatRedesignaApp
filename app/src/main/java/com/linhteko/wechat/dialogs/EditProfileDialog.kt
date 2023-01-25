package com.linhteko.wechat.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.linhteko.shared.data.vos.UserVO
import com.linhteko.shared.delegates.ColoredButtonDelegate
import com.linhteko.shared.delegates.OutlinedButtonDelegate
import com.linhteko.shared.delegates.ProfileEditDelegate
import com.linhteko.shared.utils.getDays
import com.linhteko.wechat.R
import com.linhteko.wechat.viewpods.ColoredButtonViewPod
import com.linhteko.wechat.viewpods.OutlinedButtonViewPod
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.dialog_edit_profile.*
import java.text.DateFormatSymbols
import java.util.*

class EditProfileDialog : DialogFragment() {


    private var currentMonth: Int = 0
    private var currentDay = 0
    private var currentYear = 0
    private var years: List<Int> = listOf()
    private var months: List<String> = listOf()
    private var days: List<Int> = listOf()
    private var gender: String? = null
    private var mProfileEditDelegate: ProfileEditDelegate? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mProfileEditDelegate = context as ProfileEditDelegate
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_edit_profile, container, false)
    }

    companion object {

        const val BE_USER = "user-object-for-edit-profile"

        @JvmStatic
        fun newInstance(userVO: UserVO) = EditProfileDialog().apply {
            arguments = Bundle().apply {
                putParcelable(BE_USER, userVO)
            }
        }
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

        spProfileEditYear.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_view, years)
        spProfileEditMonth.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_item_view, months)
        spProfileEditDay.adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_view, days)

        spProfileEditYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.selectedItemPosition?.let { position ->
                    currentYear = years[position]
                    if (currentMonth == calendar.get(Calendar.MONTH) && currentYear == calendar.get(
                            Calendar.YEAR
                        )
                    ) {
                        currentDay = calendar.get(Calendar.DAY_OF_MONTH) - 1
                        spProfileEditDay.setSelection(currentDay, true)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        spProfileEditMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                        spProfileEditDay.setSelection(currentDay, true)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        spProfileEditDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                p0?.selectedItemPosition?.let { position ->
                    currentDay = days[position]

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        spProfileEditYear.setSelection(currentYear, true)
        spProfileEditMonth.setSelection(currentMonth, true)
        spProfileEditDay.setSelection(currentDay, true)
    }

    private fun setUpDays(days: List<Int>) {
        spProfileEditDay.adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_view, days)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinnerData()
        arguments?.getParcelable<UserVO>(BE_USER)?.apply {
            bindUser(this)

            setUpListener()
        }
    }

    private fun setUpListener() {
        rgProfileEditGender.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbProfileEditMale -> {
                    gender = rbProfileEditMale.text.toString()
                }
                R.id.rbProfileEditFemale -> {
                    gender = rbProfileEditFemale.text.toString()
                }
                R.id.rbProfileEditOther -> {
                    gender = rbProfileEditOther.text.toString()
                }
            }
        }
    }

    private fun bindUser(userVO: UserVO) {
        etEditProfileName.setText(userVO.name ?: "")
        etEditProfilePhone.setText(userVO.phone ?: "")

        spProfileEditYear.setSelection(years.indexOf(userVO.yearOfBirth))
        spProfileEditMonth.setSelection(months.indexOf(userVO.monthOfBirth))
        spProfileEditDay.setSelection(days.indexOf(userVO.dayOfBirth))

        rgProfileEditGender.check(
            when (userVO.gender) {
                "Male" -> R.id.rbProfileEditMale
                "Female" -> R.id.rbProfileEditFemale
                else -> R.id.rbProfileEditOther
            }
        )

        setUpViewPod(userVO)
    }

    private fun setUpViewPod(userVO: UserVO) {
        (vpEditProfileCancel as OutlinedButtonViewPod).apply {
            setUpContent(getString(R.string.lbl_cancle))
            setUpDelegate(object : OutlinedButtonDelegate {
                override fun setOutlinedButtonOnClickListener() {
                    dismiss()
                }
            })
        }
        (vpEditProfileSave as ColoredButtonViewPod).apply {
            setUpContent(getString(R.string.lbl_save))
            setUpDelegate(object : ColoredButtonDelegate {
                override fun onTapColorButtonTap() {
                    validateProfile(userVO)
                }
            })
        }
    }

    private fun validateProfile(userVO: UserVO) {
        val name = etEditProfileName.text.toString()
        val phone = etEditProfilePhone.text.toString()

        if (name.isEmpty())
        {
            etEditProfileName.error = getString(R.string.lbl_required)
            return
        }
        if (phone.isEmpty())
        {
            etEditProfilePhone.error = getString(R.string.lbl_required)
            return
        }

        val user = userVO.copy(
            name = name,
            phone = phone,
            dayOfBirth = currentDay,
            monthOfBirth = months[currentMonth],
            yearOfBirth = currentYear,
            gender = gender
        )

        mProfileEditDelegate?.updateProfile(user)

    }
}