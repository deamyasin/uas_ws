package com.fooddeliveryboy.ui.splash

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.droidnet.DroidListener
import com.droidnet.DroidNet
import com.fooddeliveryboy.CommonActivity
import com.fooddeliveryboy.R
import com.fooddeliveryboy.response.CommonResponse
import com.fooddeliveryboy.ui.home.MainActivity
import com.fooddeliveryboy.ui.login.LoginActivity
import com.onesignal.OneSignal
import utils.ContextWrapper
import utils.LanguagePrefs
import utils.SessionManagement
import java.util.*

class SplashActivity : AppCompatActivity(), DroidListener {

    private lateinit var splashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonActivity.changeStatusBarColor(
            this,
            true
        )
        splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        LanguagePrefs(this)
        setContentView(R.layout.activity_splash)

        OneSignal.clearOneSignalNotifications()

        val droidNet = DroidNet.getInstance()
        droidNet.addInternetConnectivityListener(this)

    }

    private fun makeGetSettings() {
        val params = HashMap<String, String>()
        splashViewModel.makeGetSetting(params)
            .observe(this, Observer { response: CommonResponse? ->
                if (response != null) {
                    if (response.responce!!) {
                        splashViewModel.storeData(response.data!!.toString())

                        val sessionManagement = SessionManagement(this)

                        if (sessionManagement.isLoggedIn()) {
                            Intent(this, MainActivity::class.java).apply {
                                startActivity(this)
                                finish()
                            }
                        } else {
                            Intent(this, LoginActivity::class.java).apply {
                                startActivity(this)
                                finish()
                            }
                        }

                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        if (isConnected) {
            makeGetSettings()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}
