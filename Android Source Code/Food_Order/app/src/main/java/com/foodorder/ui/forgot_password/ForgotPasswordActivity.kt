package com.foodorder.ui.forgot_password

import Dialogs.LoaderDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.response.CommonResponse
import com.foodorder.ui.otp.OtpActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject
import utils.ConnectivityReceiver
import utils.SessionManagement

class ForgotPasswordActivity : CommonActivity() {

    lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordViewModel = ViewModelProvider(this)[ForgotPasswordViewModel::class.java]
        setContentView(R.layout.activity_forgot_password)
        setHeaderTitle(resources.getString(R.string.forgot_password).replace("?", ""))
        removeToolbarShadow()

        PushDownAnim.setPushDownAnimTo(btn_forgot_password_submit)

        et_forgot_password_phone.tag =
            "${SessionManagement.PermanentData.getSession(this, "mobile_prefix")}   "

        forgotPasswordViewModel.attemptForgotStatusLiveData.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                if (ConnectivityReceiver.isConnected) {
                    makeForgotPassword()
                } else {
                    ConnectivityReceiver.showSnackbar(this)
                }
            }
        })

        btn_forgot_password_submit.setOnClickListener {
            forgotPasswordViewModel.attemptPassword(et_forgot_password_phone)
        }

    }

    private fun makeForgotPassword() {
        val params = HashMap<String, String>()
        params["user_phone"] = et_forgot_password_phone.text.toString()

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        forgotPasswordViewModel.makeForgotPassword(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        val jsonObject = response.data as JSONObject

                        val userId = jsonObject.getString("user_id")
                        val userPhone = jsonObject.getString("user_phone")
                        val otp = jsonObject.getString("otp")

                        Intent(this, OtpActivity::class.java).apply {
                            putExtra("user_id", userId)
                            putExtra("user_phone", userPhone)
                            putExtra("otp", otp)
                            startActivity(this)
                            finish()
                        }

                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

}
