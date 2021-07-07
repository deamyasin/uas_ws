package com.foodorder.ui.register

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
class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    val context = application.applicationContext

    val projectRepository = ProjectRepository()
    val attemptRegisterStatusLiveData = MutableLiveData<Boolean>()

    fun attemptRegister(
        etName: EditText,
        etPhone: EditText,
        etEmail: EditText,
        etPassword: EditText,
        etPasswordRe: EditText
    ) {
        etName.error = null
        etPhone.error = null
        etEmail.error = null
        etPassword.error = null
        etPasswordRe.error = null

        var cancel: Boolean = false
        var focusView: View? = null

        val name = etName.text.toString()
        val phone = etPhone.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val passwordRe = etPasswordRe.text.toString()

        if (email.isEmpty()) {
            etEmail.error = context.resources.getString(R.string.error_field_required)
            focusView = etEmail
            cancel = true
        } else if (!CommonActivity.isEmailValid(email)) {
            etEmail.error = context.resources.getString(R.string.error_invalid_email)
            focusView = etEmail
            cancel = true
        }

        if (name.isEmpty()) {
            etName.error = context.resources.getString(R.string.error_field_required)
            focusView = etName
            cancel = true
        }

        if (passwordRe != password) {
            etPasswordRe.error = context.resources.getString(R.string.error_password_not_match)
            focusView = etPasswordRe
            cancel = true
        }

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
            attemptRegisterStatusLiveData.value = false
        } else {
            attemptRegisterStatusLiveData.value = true
        }

    }

    fun makeRegister(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.register(params)
    }

}