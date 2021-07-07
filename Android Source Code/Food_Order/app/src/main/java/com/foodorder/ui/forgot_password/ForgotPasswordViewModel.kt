package com.foodorder.ui.forgot_password

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

/**
 * Created on 04-05-2020.
 */
class ForgotPasswordViewModel(application: Application) : AndroidViewModel(application) {
    val context = application.applicationContext

    val projectRepository = ProjectRepository()
    val attemptForgotStatusLiveData = MutableLiveData<Boolean>()

    fun attemptPassword(etPhone: EditText) {
        etPhone.error = null

        var cancel: Boolean = false
        var focusView: View? = null

        val phone = etPhone.text.toString()

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
            attemptForgotStatusLiveData.value = false
        } else {
            attemptForgotStatusLiveData.value = true
        }

    }

    fun makeForgotPassword(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.forgotPassword(params)
    }

}