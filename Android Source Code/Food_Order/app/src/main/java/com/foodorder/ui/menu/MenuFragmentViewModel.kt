package com.foodorder.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse

/**
 * Created on 05-05-2020.
 */
class MenuFragmentViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeGetCategoryList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getCategoryList(params)
    }

}