package com.foodorder.ui.login

import Config.BaseURL
import Dialogs.LoaderDialog
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.response.CommonResponse
import com.foodorder.ui.forgot_password.ForgotPasswordActivity
import com.foodorder.ui.home.MainActivity
import com.foodorder.ui.otp.OtpActivity
import com.foodorder.ui.register.RegisterActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import utils.ConnectivityReceiver
import utils.SessionManagement

class LoginActivity : CommonActivity(), View.OnClickListener {

    lateinit var loaderDialog: LoaderDialog

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        setContentView(R.layout.activity_login)
        setHeaderTitle(resources.getString(R.string.login))
        removeToolbarShadow()

        PushDownAnim.setPushDownAnimTo(btn_login, btn_login_register)

        loaderDialog = LoaderDialog(this)

        et_login_phone.tag =
            "${SessionManagement.PermanentData.getSession(this, "mobile_prefix")}   "

        /*tv_login_register.makeLinks(
            Pair(
                resources.getString(R.string.new_register),
                object : View.OnClickListener {
                    override fun onClick(p0: View?) {

                    }
                })
        )*/

        loginViewModel.attemptLoginStatusLiveData.observe(this, Observer { isSuccess ->
            if (isSuccess && ConnectivityReceiver.isConnected) {
                makeLogin()
            }
        })

        btn_login.setOnClickListener(this)
        btn_login_register.setOnClickListener(this)
        tv_login_forgot.setOnClickListener(this)

    }

    fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val s = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    ds.linkColor =
                        ContextCompat.getColor(this@LoginActivity, R.color.colorGreenLight)
                    ds.color = ContextCompat.getColor(this@LoginActivity, R.color.colorGreenLight)
                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)
            s.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        this.movementMethod =
            LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
        this.setText(s, TextView.BufferType.SPANNABLE)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                loginViewModel.attemptLogin(et_login_phone, et_login_password)
                /*Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                }*/
            }
            R.id.btn_login_register -> {
                Intent(this, RegisterActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.tv_login_forgot -> {
                Intent(this, ForgotPasswordActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun makeLogin() {
        val params = HashMap<String, String>()
        params["user_phone"] = et_login_phone.text.toString()
        params["user_password"] = et_login_password.text.toString()

        loaderDialog.show()

        loginViewModel.makeLogin(params).observe(this, Observer { response: CommonResponse? ->
            if (response != null) {
                checkLoginResponse(response)
            }
        })

    }

    private fun makeGetCartList() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)

        loginViewModel.makeGetCartList(params)
            .observe(this, androidx.lifecycle.Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(
                            this@LoginActivity,
                            "cartData",
                            response.data!!.toString()
                        )
                        goNext()
                    } else {
                        goNext()
                    }
                } else {
                    goNext()
                }
            })
    }

    private fun checkLoginResponse(response: CommonResponse) {
        if (response.responce!! && response.data != null) {
            loginViewModel.dataStoreLiveData.observe(this, Observer { isStore ->
                if (isStore) {
                    makeGetCartList()
                }
            })
            loginViewModel.storeData(response.data!!.toString())
        } else {
            loaderDialog.dismiss()
            if (response.code == "108") {
                val jsonObject = JSONObject(response.data!!.toString())
                val otp = jsonObject.getString("otp")
                Intent(this, OtpActivity::class.java).apply {
                    putExtra("user_phone", et_login_phone.text.toString())
                    putExtra("otp", otp)
                    startActivity(this)
                }
            } else {
                CommonActivity.showToast(this, response.message!!)
            }
        }
    }

    private fun goNext() {
        if (intent.getBooleanExtra("isFinish", false)) {
            Intent().apply {
                setResult(Activity.RESULT_OK, this)
                finish()
            }
        } else {
            Intent(this@LoginActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
                finishAffinity()
            }
        }
    }

}
