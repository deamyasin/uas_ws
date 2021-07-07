package com.foodorder.ui.change_reset_password

import android.app.Application
import android.view.View
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foodorder.R
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse

/**
 * Created on 04-05-2020.
 */
class ChangeResetPasswordViewModel(application: Application) : AndroidViewModel(application) {
    val context = application.applicationContext

    val projectRepository = ProjectRepository()
    val attemptPasswordStatusLiveData = MutableLiveData<Boolean>()

    fun attemptPassword(
        etPasswordOld: EditText?,
        etPasswordNew: EditText,
        etPasswordNewCon: EditText
    ) {
        etPasswordOld?.error = null
        etPasswordNew.error = null
        etPasswordNewCon.error = null

        var cancel: Boolean = false
        var focusView: View? = null

        val passwordOld = etPasswordOld?.text.toString()
        val passwordNew = etPasswordNew.text.toString()
        val passwordNewCon = etPasswordNewCon.text.toString()

        if (passwordNewCon != passwordNew) {
            etPasswordNewCon.error =
                context.resources.getString(R.string.error_password_not_match)
            focusView = etPasswordNewCon
            cancel = true
        }

        if (passwordNew.isEmpty()) {
            etPasswordNew.error = context.resources.getString(R.string.error_field_required)
            focusView = etPasswordNew
            cancel = true
        }

        if (etPasswordOld != null && passwordOld.isEmpty()) {
            etPasswordOld.error = context.resources.getString(R.string.error_field_required)
            focusView = etPasswordOld
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
            attemptPasswordStatusLiveData.value = false
        } else {
            attemptPasswordStatusLiveData.value = true
        }

    }

    fun makeChangePassword(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.changePassword(params)
    }

    fun makeResetPassword(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.resetPassword(params)
    }

}