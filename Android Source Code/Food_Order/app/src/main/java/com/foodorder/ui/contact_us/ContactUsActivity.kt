package com.foodorder.ui.contact_us

import Config.BaseURL
import Dialogs.LoaderDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.response.CommonResponse
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_contact_us.*
import utils.ConnectivityReceiver
import utils.SessionManagement

class ContactUsActivity : CommonActivity(), View.OnClickListener {

    lateinit var contactUsViewModel: ContactUsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactUsViewModel = ViewModelProvider(this)[ContactUsViewModel::class.java]
        setContentView(R.layout.activity_contact_us)

        setHeaderTitle(resources.getString(R.string.contact_us))
        removeToolbarShadow()

        PushDownAnim.setPushDownAnimTo(
            btn_contact_us_send,
            iv_contact_call,
            iv_contact_email,
            iv_contact_whatsapp
        )

        contactUsViewModel.isContactValid.observe(this, Observer { isValid ->
            if (isValid) {
                sendContactData()
            }
        })

        btn_contact_us_send.setOnClickListener(this)
        iv_contact_call.setOnClickListener(this)
        iv_contact_email.setOnClickListener(this)
        iv_contact_whatsapp.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_contact_us_send -> {
                contactUsViewModel.attemptContactUs(
                    this,
                    et_contact_us_full_name,
                    et_contact_us_phone,
                    et_contact_us_message
                )
            }
            R.id.iv_contact_call -> {
                contactUsViewModel.intentCall(this)
            }
            R.id.iv_contact_email -> {
                contactUsViewModel.intentEmail(this)
            }
            R.id.iv_contact_whatsapp -> {
                contactUsViewModel.intentWhatsapp(this)
            }
        }
    }

    private fun sendContactData() {
        if (ConnectivityReceiver.isConnected) {
            val params = HashMap<String, String>()
            params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
            params["fullname"] = et_contact_us_full_name.text.toString()
            params["phone"] = et_contact_us_phone.text.toString()
            params["message"] = et_contact_us_message.text.toString()

            val loaderDialog = LoaderDialog(this)
            loaderDialog.show()

            contactUsViewModel.makeSendContact(params)
                .observe(this, Observer { response: CommonResponse? ->
                    loaderDialog.dismiss()
                    if (response != null) {
                        if (response.responce!!) {
                            CommonActivity.showToast(this, response.data!!.toString())
                            finish()
                        } else {
                            CommonActivity.showToast(this, response.message!!)
                        }
                    }
                })
        } else {
            ConnectivityReceiver.showSnackbar(this)
        }
    }

}
