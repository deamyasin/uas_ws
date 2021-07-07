package com.foodorder.ui.order_detail

import Dialogs.CommonAlertDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foodorder.R
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse

/**
 * Created on 08-05-2020.
 */
class OrderDetailViewModel : ViewModel() {
    val projectRepository = ProjectRepository()
    val reorderAlertLiveData = MutableLiveData<Boolean>()

    fun makeGetOrderDetail(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.orderDetail(params)
    }

    fun makeReOrder(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.reOrder(params)
    }

    fun makeCancelOrder(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.cancelOrder(params)
    }

    fun showReorderAlert(context: Context) {
        CommonAlertDialog(context,
            context.resources.getString(R.string.are_you_sure_you_want_to_clear_cart),
            context.resources.getString(R.string.if_you_re_order_your_product_than_old_cart_data_is_cleared_and_re_order_data_will_added),
            null,
            null,
            object : CommonAlertDialog.OnClickListener {
                override fun cancelClick() {
                    reorderAlertLiveData.value = false
                }

                override fun okClick() {
                    reorderAlertLiveData.value = true
                }
            }).show()
    }

}