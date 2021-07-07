package com.foodorder.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created on 10-02-2020.
 */
class CommonResponse {
    @SerializedName("responce")
    @Expose
    var responce: Boolean? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    var data: Any? = null
}