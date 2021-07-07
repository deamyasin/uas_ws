package com.fooddeliveryboy.ui.order_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fooddeliveryboy.repository.ProjectRepository
import com.fooddeliveryboy.response.CommonResponse
import com.fooddeliveryboy.response.OrderItemResponse

/**
 * Created on 06-04-2020.
 */
class OrderDetailViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeGetOrderDetail(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getOrderDetail(params)
    }

}