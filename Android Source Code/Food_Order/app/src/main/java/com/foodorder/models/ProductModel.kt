package com.foodorder.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created on 05-05-2020.
 */
class ProductModel : Serializable {

    val product_id: String? = null
    val category_id: String? = null
    val product_name_en: String? = null
    val product_name_ar: String? = null
    val product_desc_en: String? = null
    val product_desc_ar: String? = null
    val product_extra_en: String? = null
    val product_extra_ar: String? = null
    val level: String? = null
    val is_veg: String? = null
    val product_image: String? = null
    val is_promotional: String? = null
    val price: String? = null
    val price_note: String? = null
    val calories: String? = null
    val calories_en: String? = null
    val calories_ar: String? = null
    val status: String? = null
    val created_at: String? = null
    val modified_at: String? = null
    val created_by: String? = null
    val modified_by: String? = null
    val draft: String? = null
    val cat_name_en: String? = null
    val cat_name_ar: String? = null
    val options: String? = null
    val cart_qty: String? = null
    val discount: String? = null
    val discount_type: String? = null
    val product_discount_id: String? = null

    @SerializedName(value = "options_list", alternate = ["product_options"])
    val optionModelList: ArrayList<ProductOptionModel>? = null

    val cart_id: String? = null
    val user_id: String? = null
    val qty: String? = null
    val effected_price: String? = null

}