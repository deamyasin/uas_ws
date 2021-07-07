package com.foodorder.models

import java.io.Serializable

/**
 * Created on 06-05-2020.
 */
class ProductOptionModel : Serializable {

    val product_option_id: String? = null
    val product_id: String? = null
    val option_name_en: String? = null
    val option_name_ar: String? = null
    val option_desc_en: String? = null
    val option_desc_ar: String? = null
    val price: String? = null
    var cart_qty: String = "0"
    var multiple: String = "0"

    var isLoading: Boolean = false
    var qty: String = "0"

}