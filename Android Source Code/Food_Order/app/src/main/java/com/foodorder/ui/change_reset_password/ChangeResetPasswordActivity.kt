package com.foodorder.ui.change_reset_password

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
import kotlinx.android.synthetic.main.activity_change_reset_password.*
import utils.ConnectivityReceiver
import utils.SessionManagement

class ChangeResetPasswordActivity : CommonActivity() {


    lateinit var changeResetPasswordViewModel: ChangeResetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeResetPasswordViewModel =
            ViewModelProvider(this)[ChangeResetPasswordViewModel::class.java]
        setContentView(R.layout.activity_change_reset_password)
        removeToolbarShadow()

        PushDownAnim.setPushDownAnimTo(btn_change_password_submit)

        val isReset = intent.hasExtra("user_id")

        if (isReset) {
            setHeaderTitle(resources.getString(R.string.reset_password))
            et_change_password_old.visibility = View.GONE
        } else {
            setHeaderTitle(resources.getString(R.string.change_password))
        }

        changeResetPasswordViewModel.attemptPasswordStatusLiveData.observe(
            this,
            Observer { isSuccess ->
                if (isSuccess) {
                    if (ConnectivityReceiver.isConnected) {
                        if (isReset) {
                            makeResetPassword()
                        } else {
                            makeChangePassword()
                        }
                    } else {
                        ConnectivityReceiver.showSnackbar(this)
                    }
                }
            })

        btn_change_password_submit.setOnClickListener {
            changeResetPasswordViewModel.attemptPassword(
                if (isReset) null else et_change_password_old,
                et_change_password_new,
                et_change_password_new_con
            )
        }

    }

    private fun makeChangePassword() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["c_password"] = et_change_password_old.text.toString()
        params["n_password"] = et_change_password_new.text.toString()
        params["r_password"] = et_change_password_new_con.text.toString()

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        changeResetPasswordViewModel.makeChangePassword(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        CommonActivity.showToast(this, response.message!!)
                        finish()
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeResetPassword() {
        val params = HashMap<String, String>()
        params["user_id"] = intent.getStringExtra("user_id")!!
        params["n_password"] = et_change_password_new.text.toString()
        params["r_password"] = et_change_password_new_con.text.toString()

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        changeResetPasswordViewModel.makeResetPassword(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        CommonActivity.showToast(this, response.message!!)
                        finish()
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

}
