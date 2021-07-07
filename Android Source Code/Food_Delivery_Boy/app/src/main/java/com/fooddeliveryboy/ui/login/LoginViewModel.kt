package com.fooddeliveryboy.ui.login

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fooddeliveryboy.CommonActivity
import com.fooddeliveryboy.R
import com.fooddeliveryboy.repository.ProjectRepository
import com.fooddeliveryboy.response.CommonResponse
import org.json.JSONObject
import utils.SessionManagement

/**
 * Created on 06-04-2020.
 */
class LoginViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    val getAttemptStatus = MutableLiveData<Boolean>()

    fun attemptLogin(context: Context, etNumber: EditText, etPassword: EditText) {
        etNumber.error = null
        etPassword.error = null

        val number = etNumber.text.toString()
        val password = etPassword.text.toString()

        var focusView: View? = null
        var cancel = false

        if (password.isEmpty()) {
            etPassword.error = context.resources.getString(R.string.error_field_required)
            focusView = etPassword
            cancel = true
        } else if (!CommonActivity.isPasswordValid(password)) {
            etPassword.error = context.resources.getString(R.string.error_password_short)
            focusView = etPassword
            cancel = true
        }

        if (number.isEmpty()) {
            etNumber.error = context.resources.getString(R.string.error_field_required)
            focusView = etNumber
            cancel = true
        } else if (!CommonActivity.isPhoneValid(number)) {
            etNumber.error = context.resources.getString(R.string.error_phone_short)
            focusView = etNumber
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
            getAttemptStatus.value = false
        } else {
            getAttemptStatus.value = true
        }
    }

    fun makeLogin(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.checkLogin(params)
    }

    fun storeLoginData(context: Context, response: String) {
        val jsonObject = JSONObject(response)

        val deliveryBoyId = jsonObject.getString("delivery_boy_id")
        val boyPhone = jsonObject.getString("boy_phone")
        val boyName = jsonObject.getString("boy_name")
        val boyEmail = jsonObject.getString("boy_email")
        val boyPhoto = jsonObject.getString("boy_photo")
        val vehicleId = jsonObject.getString("vehicle_no")
        val licenceNo = jsonObject.getString("boy_licence")
        val idProof = jsonObject.getString("boy_id_proof")
        val idPhoto = jsonObject.getString("id_photo")
        val licencePhoto = jsonObject.getString("licence_photo")
        val status = jsonObject.getString("status")

        val sessionManagement = SessionManagement(context)

        sessionManagement.createLoginSession(
            deliveryBoyId,
            "1",
            boyEmail,
            boyName,
            boyPhone,
            boyPhoto
        )

        SessionManagement.UserData.setSession(context, "vehicle_id", vehicleId)
        SessionManagement.UserData.setSession(context, "licence_no", licenceNo)
        SessionManagement.UserData.setSession(context, "id_proof", idProof)
        SessionManagement.UserData.setSession(context, "licence_photo", licencePhoto)
        SessionManagement.UserData.setSession(context, "id_photo", idPhoto)
        SessionManagement.UserData.setSession(context, "status", status)

    }

}