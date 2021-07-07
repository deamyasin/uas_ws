package com.fooddeliveryboy.ui.profile

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

/**
 * Created on 06-04-2020.
 */
class ChangePasswordViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    val getAttemptStatus = MutableLiveData<Boolean>()

    fun attemptChangePassword(
        context: Context,
        etPasswordOld: EditText,
        etPasswordNew: EditText,
        etPasswordCon: EditText
    ) {
        etPasswordOld.error = null
        etPasswordNew.error = null
        etPasswordCon.error = null

        val passwordOld = etPasswordOld.text.toString()
        val passwordNew = etPasswordNew.text.toString()
        val passwordCon = etPasswordCon.text.toString()

        var focusView: View? = null
        var cancel = false

        if (passwordNew.isNotEmpty() && passwordNew != passwordCon) {
            etPasswordCon.error =
                context.resources.getString(R.string.confirm_password_does_t_match_with_new_password)
            focusView = etPasswordCon
            cancel = true
        }

        if (passwordNew.isEmpty()) {
            etPasswordNew.error = context.resources.getString(R.string.error_field_required)
            focusView = etPasswordNew
            cancel = true
        } else if (!CommonActivity.isPasswordValid(passwordNew)) {
            etPasswordNew.error = context.resources.getString(R.string.error_password_short)
            focusView = etPasswordNew
            cancel = true
        }

        if (passwordOld.isEmpty()) {
            etPasswordOld.error = context.resources.getString(R.string.error_field_required)
            focusView = etPasswordOld
            cancel = true
        } else if (!CommonActivity.isPasswordValid(passwordOld)) {
            etPasswordOld.error = context.resources.getString(R.string.error_password_short)
            focusView = etPasswordOld
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
            getAttemptStatus.value = false
        } else {
            getAttemptStatus.value = true
        }
    }

    fun makeChangePassword(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.changePassword(params)
    }

}