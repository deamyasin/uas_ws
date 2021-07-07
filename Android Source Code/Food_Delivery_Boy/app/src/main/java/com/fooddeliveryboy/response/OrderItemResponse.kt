package com.fooddeliveryboy.response

import com.fooddeliveryboy.model.OrderItemModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created on 10-02-2020.
 */
class OrderItemResponse {
    @Expose
    @SerializedName("responce")
    var responce: Boolean? = null

    @Expose
    @SerializedName("error")
    var message: String? = null
    var data: String? = null
    var orderItemModelList: ArrayList<OrderItemModel>? = null
}