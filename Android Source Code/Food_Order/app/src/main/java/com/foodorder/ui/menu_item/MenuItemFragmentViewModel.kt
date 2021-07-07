package com.foodorder.ui.menu_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse

/**
 * Created on 05-05-2020.
 */
class MenuItemFragmentViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeGetProductList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getProductList(params)
    }

    fun makeGetSearchProductList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getSearchProductList(params)
    }

}