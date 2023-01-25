package com.linhteko.wechat.dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.linhteko.wechat.R
import kotlinx.android.synthetic.main.dialog_qr_code.*
import kotlinx.android.synthetic.main.fragment_profile.*

class QrCodeDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_qr_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(ARG_USER_ID)?.let {
            encodeQrCode(it)
        }
    }

    private fun encodeQrCode(uid: String) {
        val encoder = BarcodeEncoder()
        val bitmap = encoder.encodeBitmap(
            uid,
            BarcodeFormat.QR_CODE,
            resources.getDimension(R.dimen.size_big_qr).toInt(),
            resources.getDimension(R.dimen.size_big_qr).toInt()
        )
        ivQrCode.setImageBitmap(bitmap)
    }

    companion object {
        const val ARG_USER_ID = "user-id-for-qrcode"
        @JvmStatic
        fun newInstance(userId: String) =
            QrCodeDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER_ID, userId)
                }
            }
    }
}