package com.foodorder.ui.splash

import Config.BaseURL
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.response.CommonResponse
import com.foodorder.ui.home.MainActivity
import com.foodorder.ui.login.LoginActivity
import com.onesignal.OneSignal
import utils.ConnectivityReceiver
import utils.SessionManagement
import java.util.HashMap

class SplashActivity : AppCompatActivity() {

    private var isSettingDone = false
    private var isCartDone = false

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonActivity.changeStatusBarColor(this, true)
        splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        setContentView(R.layout.activity_splash)

        OneSignal.clearOneSignalNotifications()

        if (ConnectivityReceiver.isConnected) {
            makeGetSettings()
            if (SessionManagement.UserData.isLogin(this)) {
                makeGetCartList()
            } else {
                isCartDone = true
            }
        } else {
            ConnectivityReceiver.showSnackbar(this)
        }

    }

    private fun makeGetSettings() {
        val params = HashMap<String, String>()
        splashViewModel.makeGetSetting(params)
            .observe(this, Observer { response: CommonResponse? ->
                if (response != null) {
                    if (response.responce!!) {
                        splashViewModel.storeData(response.data!!.toString())
                        isSettingDone = true
                        goNext()
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeGetCartList() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)

        splashViewModel.makeGetCartList(params)
            .observe(this, Observer { response: CommonResponse? ->
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(
                            this,
                            "cartData",
                            response.data!!.toString()
                        )
                        isCartDone = true
                        goNext()
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun goNext() {
        if (isSettingDone && isCartDone) {
            //if (SessionManagement.UserData.isLogin(this)) {
            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
                finish()
            }
            /*} else {
                Intent(this, LoginActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
            }*/
        }
    }

}
