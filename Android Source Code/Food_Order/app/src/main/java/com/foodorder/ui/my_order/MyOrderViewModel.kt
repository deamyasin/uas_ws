package com.foodorder.ui.my_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse

/**
 * Created on 08-05-2020.
 */
class MyOrderViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeGetOrderList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.orderList(params)
    }

}