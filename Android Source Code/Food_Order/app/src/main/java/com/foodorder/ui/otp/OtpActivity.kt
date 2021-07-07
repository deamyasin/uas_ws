package com.foodorder.ui.otp

import Config.BaseURL
import Dialogs.LoaderDialog
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mukesh.OtpView
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.response.CommonResponse
import com.foodorder.ui.change_reset_password.ChangeResetPasswordActivity
import com.foodorder.ui.home.MainActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_otp.*
import org.json.JSONObject
import utils.ConnectivityReceiver
import utils.SessionManagement


class OtpActivity : CommonActivity() {

    var user_phone: String = ""

    lateinit var otpViewModel: OtpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpViewModel = ViewModelProvider(this).get(OtpViewModel::class.java)
        setContentView(R.layout.activity_otp)
        setHeaderTitle(getString(R.string.verify_mobile))
        removeToolbarShadow()

        user_phone = intent.getStringExtra("user_phone")!!

        PushDownAnim.setPushDownAnimTo(btn_otp_verify)

        tv_otp_phone.text = user_phone

        btn_otp_verify.isEnabled = false

        val otpView = otpview as OtpView

        otpView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //afterTextChanged
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //beforeTextChanged
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btn_otp_verify.isEnabled = otpView.text.toString().length == 6
            }
        })

        otpViewModel.countDownLiveData.observe(this, Observer { time ->
            val timer = "$time ${resources.getString(R.string.in_sec)}"

            val timerText = SpannableStringBuilder()
            timerText.append(resources.getString(R.string.resend_in))
            timerText.append(" ")
            timerText.append(timer)

            val startPosition: Int = timerText.length - timer.length
            val endPosition = timerText.length
            timerText.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        this,
                        R.color.colorRed
                    )
                ),
                startPosition,
                endPosition,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tv_otp_timer.text = timerText
        })
        otpViewModel.countDownFinishLiveData.observe(this, Observer { isFinish ->
            if (isFinish) {
                tv_otp_timer.visibility = View.GONE
                btn_otp_resend.visibility = View.VISIBLE
            } else {
                tv_otp_timer.visibility = View.VISIBLE
                btn_otp_resend.visibility = View.GONE
            }
        })
        otpViewModel.startTimer()

        btn_otp_verify.setOnClickListener {
            if (ConnectivityReceiver.isConnected) {
                if (SessionManagement.UserData.isLogin(this)) {
                    makeVerifyUpdatePhone()
                } else if (intent.hasExtra("user_id")) {
                    makeVerifyForgotPhone()
                } else {
                    makeVerifyPhone()
                }
            } else {
                ConnectivityReceiver.showSnackbar(this)
            }
        }

        btn_otp_resend.setOnClickListener {
            if (ConnectivityReceiver.isConnected) {
                makeResendOtp()
            } else {
                ConnectivityReceiver.showSnackbar(this)
            }
        }

    }

    private fun makeVerifyPhone() {
        val params = HashMap<String, String>()
        params["user_phone"] = user_phone
        params["otp"] = otpview.text.toString()

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        otpViewModel.makeVerifyPhone(params).observe(this, Observer { response: CommonResponse? ->
            loaderDialog.dismiss()
            if (response != null) {
                if (response.responce!!) {
                    otpViewModel.dataStoreLiveData.observe(this, Observer { isStore ->
                        if (isStore) {
                            Intent(this@OtpActivity, MainActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(this)
                                finishAffinity()
                            }
                        }
                    })
                    otpViewModel.storeData(response.data!!.toString())
                } else {
                    CommonActivity.showToast(this, response.message!!)
                }
            }
        })
    }

    private fun makeVerifyForgotPhone() {
        val params = HashMap<String, String>()
        params["user_phone"] = user_phone
        params["otp"] = otpview.text.toString()

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        otpViewModel.makeVerifyForgotPhone(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        val jsonObject = JSONObject(response.data!!.toString())

                        val userId = jsonObject.getString("user_id")
                        val userPhone = jsonObject.getString("user_phone")

                        Intent(this, ChangeResetPasswordActivity::class.java).apply {
                            putExtra("user_id", userId)
                            putExtra("user_phone", userPhone)
                            startActivity(this)
                            finish()
                        }

                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeVerifyUpdatePhone() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["user_phone"] = user_phone
        params["otp"] = otpview.text.toString()

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        otpViewModel.makeVerifyUpdatePhone(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        val jsonObject = JSONObject(response.data!!.toString())

                        val userPhone = jsonObject.getString("user_phone")

                        SessionManagement.UserData.setSession(
                            this,
                            BaseURL.KEY_MOBILE,
                            userPhone
                        )

                        Intent().apply {
                            setResult(Activity.RESULT_OK, this)
                            finish()
                        }

                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeResendOtp() {
        val params = HashMap<String, String>()
        params["user_phone"] = user_phone

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        otpViewModel.makeResendOtp(params).observe(this, Observer { response: CommonResponse? ->
            loaderDialog.dismiss()
            if (response != null) {
                if (response.responce!!) {
                    otpViewModel.startTimer()
                } else {
                    CommonActivity.showToast(this, response.message!!)
                }
            }
        })
    }

}
