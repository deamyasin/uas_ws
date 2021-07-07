package com.foodorder.ui.register

import Config.BaseURL
import Dialogs.LoaderDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.response.CommonResponse
import com.foodorder.ui.otp.OtpActivity
import com.foodorder.ui.webview.CommonWebViewActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.bottom_sheet_edit_profile.view.*
import org.json.JSONObject
import utils.ConnectivityReceiver
import utils.SessionManagement
import utils.TextInputFilter

class RegisterActivity : CommonActivity(), View.OnClickListener {

    lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        setContentView(R.layout.activity_register)
        setHeaderTitle(getString(R.string.register_new))
        removeToolbarShadow()

        PushDownAnim.setPushDownAnimTo(btn_register, btn_register_login)

        et_register_email.filters = arrayOf<InputFilter>(
            TextInputFilter(
                TextInputFilter.Filter.ALLOW_EMAIL_ONLY
            ), InputFilter.LengthFilter(50)
        )

        et_register_phone.tag =
            "${SessionManagement.PermanentData.getSession(this, "mobile_prefix")}   "

        registerViewModel.attemptRegisterStatusLiveData.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                if (iv_register_terms_check.isSelected) {
                    if (ConnectivityReceiver.isConnected) {
                        makeRegister()
                    }
                } else {
                    CommonActivity.showToast(
                        this,
                        resources.getString(R.string.agree_with_terms_and_conditions)
                    )
                }
            }
        })

        iv_register_terms_check.setOnClickListener(this)
        tv_register_terms.setOnClickListener(this)
        iv_register_terms.setOnClickListener(this)
        btn_register.setOnClickListener(this)
        btn_register_login.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_register_terms_check, R.id.tv_register_terms -> {
                if (iv_register_terms_check.isSelected) {
                    iv_register_terms_check.isSelected = false
                    iv_register_terms_check.setImageResource(R.mipmap.ic_checkbox_uncheck)
                } else {
                    iv_register_terms_check.isSelected = true
                    iv_register_terms_check.setImageResource(R.mipmap.ic_checkbox_check)
                }
            }
            R.id.iv_register_terms -> {
                Intent(this, CommonWebViewActivity::class.java).apply {
                    putExtra("title", resources.getString(R.string.terms_and_conditions))
                    putExtra("url", BaseURL.TNC_URL)
                    startActivity(this)
                }
            }
            R.id.btn_register -> {
                registerViewModel.attemptRegister(
                    et_register_full_name,
                    et_register_phone,
                    et_register_email,
                    et_register_password,
                    et_register_password_re
                )
            }
            R.id.btn_register_login -> {
                finish()
            }
        }
    }

    private fun makeRegister() {
        val params = HashMap<String, String>()
        params["user_email"] = et_register_email.text.toString()
        params["user_phone"] = et_register_phone.text.toString()
        params["user_fullname"] = et_register_full_name.text.toString()
        params["user_password"] = et_register_password.text.toString()
        params["android_token"] = ""
        params["ios_token"] = ""

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        registerViewModel.makeRegister(params).observe(this, Observer { response: CommonResponse? ->
            loaderDialog.dismiss()
            if (response != null) {
                if (response.responce!!) {
                    Intent(this, OtpActivity::class.java).apply {
                        putExtra("user_phone", et_register_phone.text.toString())
                        startActivity(this)
                    }
                } else {
                    if (response.code == "108") {
                        val jsonObject = JSONObject(response.data!!.toString())
                        val otp = jsonObject.getString("otp")
                        Intent(this, OtpActivity::class.java).apply {
                            putExtra("user_phone", et_register_phone.text.toString())
                            putExtra("otp", otp)
                            startActivity(this)
                        }
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            }
        })

    }

}
