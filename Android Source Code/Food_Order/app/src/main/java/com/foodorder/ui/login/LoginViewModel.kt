package com.foodorder.ui.login

import android.app.Application
import android.view.View
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse
import org.json.JSONObject
import utils.SessionManagement

/**
 * Created on 04-05-2020.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val context = application.applicationContext

    val projectRepository = ProjectRepository()
    val attemptLoginStatusLiveData = MutableLiveData<Boolean>()
    val dataStoreLiveData = MutableLiveData<Boolean>()

    fun attemptLogin(etPhone: EditText, etPassword: EditText) {
        etPhone.error = null
        etPassword.error = null

        var cancel: Boolean = false
        var focusView: View? = null

        val phone = etPhone.text.toString()
        val password = etPassword.text.toString()

        if (password.isEmpty()) {
            etPassword.error = context.resources.getString(R.string.error_field_required)
            focusView = etPassword
            cancel = true
        }

        if (phone.isEmpty()) {
            etPhone.error = context.resources.getString(R.string.error_field_required)
            focusView = etPhone
            cancel = true
        } else if (!CommonActivity.isPhoneValid(phone)) {
            etPhone.error = context.resources.getString(R.string.error_phone_too_short)
            focusView = etPhone
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
            attemptLoginStatusLiveData.value = false
        } else {
            attemptLoginStatusLiveData.value = true
        }

    }

    fun makeLogin(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.checkLogin(params)
    }

    fun makeGetCartList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getCartList(params)
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