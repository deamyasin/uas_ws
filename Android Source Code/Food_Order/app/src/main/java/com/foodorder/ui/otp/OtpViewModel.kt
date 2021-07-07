package com.foodorder.ui.otp

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse
import org.json.JSONObject
import utils.SessionManagement


/**
 * Created on 02-05-2020.
 */
class OtpViewModel(application: Application) : AndroidViewModel(application) {
    val context = application.applicationContext

    val projectRepository = ProjectRepository()
    val countDownLiveData = MutableLiveData<Long>()
    val countDownFinishLiveData = MutableLiveData<Boolean>()
    val dataStoreLiveData = MutableLiveData<Boolean>()
    var countDownTimer: CountDownTimer? = null

    fun startTimer() {
        countDownTimer = object : CountDownTimer(31000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countDownLiveData.value = millisUntilFinished / 1000
                countDownFinishLiveData.value = false
            }

            override fun onFinish() {
                countDownFinishLiveData.value = true
            }
        }.start()
    }

    fun makeVerifyPhone(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.verifyPhone(params)
    }

    fun makeVerifyForgotPhone(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.verifyForgotPassword(params)
    }

    fun makeVerifyUpdatePhone(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.verifyUpdatePhone(params)
    }

    fun makeResendOtp(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.resendOtp(params)
    }

    fun storeData(data: String) {
        val jsonObject = JSONObject(data)

        val userId = jsonObject.getString("user_id")
        val userTypeId = jsonObject.getString("user_type_id")
        val userEmail = jsonObject.getString("user_email")
        val userFirstname = jsonObject.getString("user_firstname")
        val userLastname = jsonObject.getString("user_lastname")
        val userPhone = jsonObject.getString("user_phone")
        val userImage = jsonObject.getString("user_image")
        val userCompanyName = jsonObject.getString("user_company_name")
        val userCompanyId = jsonObject.getString("user_company_id")
        val isEmailVerified = jsonObject.getString("is_email_verified")
        val isMobileVerified = jsonObject.getString("is_mobile_verified")
        val status = jsonObject.getString("status")

        val sessionManagement = SessionManagement(context = context)
        sessionManagement.createLoginSession(
            userId,
            userTypeId,
            userEmail,
            "$userFirstname $userLastname",
            userPhone,
            userImage
        )

        SessionManagement.UserData.setSession(context, "user_company_name", userCompanyName)
        SessionManagement.UserData.setSession(context, "user_company_id", userCompanyId)
        SessionManagement.UserData.setSession(context, "is_email_verified", isEmailVerified)
        SessionManagement.UserData.setSession(context, "is_mobile_verified", isMobileVerified)
        SessionManagement.UserData.setSession(context, "status", status)

        dataStoreLiveData.value = true
    }

}