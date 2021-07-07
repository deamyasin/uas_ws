package com.foodorder.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse

/**
 * Created on 12-05-2020.
 */
class CheckoutNoteViewModel :ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeValidCoupon(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.validCoupon(params)
    }
}