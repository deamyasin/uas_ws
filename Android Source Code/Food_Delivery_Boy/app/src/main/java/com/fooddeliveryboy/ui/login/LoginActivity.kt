package com.fooddeliveryboy.ui.login

import Config.BaseURL
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fooddeliveryboy.CommonActivity
import com.fooddeliveryboy.R
import com.fooddeliveryboy.response.CommonResponse
import com.fooddeliveryboy.ui.home.MainActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import dialogs.LoaderDialog
import kotlinx.android.synthetic.main.activity_login.*
import utils.ContextWrapper
import utils.LanguagePrefs
import java.util.*
import kotlin.collections.HashMap

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonActivity.changeStatusBarColor(this, true)
        LanguagePrefs(this)
        setContentView(R.layout.activity_login)

        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        PushDownAnim.setPushDownAnimTo(btn_login)

        loginViewModel.getAttemptStatus.observe(this, Observer { status: Boolean ->
            if (status) {
                val params = HashMap<String, String>()
                params["boy_phone"] = et_login_phone_number.text.toString()
                params["boy_password"] = et_login_password.text.toString()

                val loaderDialog = LoaderDialog(this)
                loaderDialog.show()

                loginViewModel.makeLogin(params)
                    .observe(this, Observer { response: CommonResponse? ->
                        loaderDialog.dismiss()
                        if (response != null) {
                            if (response.responce!!) {
                                loginViewModel.storeLoginData(this, response.data!!.toString())
                                Intent(this, MainActivity::class.java).apply {
                                    startActivity(this)
                                    finish()
                                }
                            } else {
                                CommonActivity.showToast(this, response.message!!)
                            }
                        }
                    })
            }
        })

        tv_login_en.setOnClickListener(this)
        tv_login_ar.setOnClickListener(this)
        btn_login.setOnClickListener {
            loginViewModel.attemptLogin(this, et_login_phone_number, et_login_password)
        }

    }

    override fun onClick(v: View) {
        val languagePrefs = LanguagePrefs(this)
        when (v.id) {
            R.id.tv_login_en -> {
                languagePrefs.saveLanguage("en")
                languagePrefs.initLanguage("en")
                recreate()
            }
            R.id.tv_login_ar -> {
                languagePrefs.saveLanguage("ar")
                languagePrefs.initLanguage("ar")
                recreate()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (LanguagePrefs.getLang(this) == "en") {
            tv_login_en.isSelected = true
            tv_login_ar.isSelected = false
            tv_login_en.setBackgroundResource(R.drawable.xml_view_rounded_green)
            tv_login_ar.setBackgroundResource(R.color.colorWhite)
        } else {
            tv_login_en.isSelected = false
            tv_login_ar.isSelected = true
            tv_login_en.setBackgroundResource(R.color.colorWhite)
            tv_login_ar.setBackgroundResource(R.drawable.xml_view_rounded_green)
        }

        if (BaseURL.ALLOW_LANGUAGE) {
            cv_login_language.visibility = View.VISIBLE
        } else {
            cv_login_language.visibility = View.GONE
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}
