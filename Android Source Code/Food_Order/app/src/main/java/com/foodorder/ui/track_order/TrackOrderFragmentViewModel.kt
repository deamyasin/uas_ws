package com.foodorder.ui.track_order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse

/**
 * Created on 11-05-2020.
 */
class TrackOrderFragmentViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeGetTrackOrderList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getTrackOrderList(params)
    }

}