package Interfaces

import com.foodorder.models.CouponModel

/**
 * Created on 07-02-2020.
 */
interface OnCouponSelected {
    fun onSelected(couponModel: CouponModel)
}