package com.foodorder.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse

/**
 * Created on 08-05-2020.
 */
class CheckoutViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeSendOrder(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.sendOrder(params)
    }

    fun makeGetPaymentData(paymentUrl: String): LiveData<String?> {
        return projectRepository.getPaymentData(paymentUrl)
    }

    fun makeGetPaymentDataFinal(paymentUrl: String): LiveData<String?> {
        return projectRepository.getPaymentData(paymentUrl)
    }

}