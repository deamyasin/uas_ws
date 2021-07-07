package com.foodorder.ui.home.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse
import kotlin.math.abs

class HomeFragmentViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeGetCategoryList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getCategoryList(params)
    }

    fun makeGetHomeList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getHomeList(params)
    }

    fun closestNumber(n: Int, m: Int): Int { // find the quotient
        val q = n / m
        // 1st possible closest number
        val n1 = m * q
        // 2nd possible closest number
        val n2 = if (n * m > 0) m * (q + 1) else m * (q - 1)
        // if true, then n1 is the required closest number
        return if (abs(n - n1) < abs(n - n2)) n1 else n2
        // else n2 is the required closest number
    }

}