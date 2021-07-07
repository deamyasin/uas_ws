package com.foodorder.ui.item_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse

/**
 * Created on 05-05-2020.
 */
class ItemDetailViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeGetProductDetail(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getProductDetail(params)
    }

    fun makeAddCartItem(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.addCartItem(params)
    }

    fun makeRemoveCartItem(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.removeCartItem(params)
    }

    fun makeAddCartItemOption(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.addCartItemOption(params)
    }

    fun makeRemoveCartItemOption(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.removeCartItemOption(params)
    }

}