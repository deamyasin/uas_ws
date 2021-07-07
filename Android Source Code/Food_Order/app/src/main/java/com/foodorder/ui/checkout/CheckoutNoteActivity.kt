package com.foodorder.ui.checkout

import Interfaces.OnCouponSelected
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.CouponModel
import com.foodorder.ui.checkout.dialog.BottomSheetCheckoutCouponDialog
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_checkout_note.*
import java.io.Serializable

class CheckoutNoteActivity : CommonActivity(), View.OnClickListener {

    var couponModel: CouponModel? = null
    var totalDiscountPrice: Double = 0.0
    var branch_id: String = ""
    var user_address_id: String = ""

    lateinit var checkoutNoteViewModel: CheckoutNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutNoteViewModel = ViewModelProvider(this)[CheckoutNoteViewModel::class.java]
        setContentView(R.layout.activity_checkout_note)
        setHeaderTitle(resources.getString(R.string.confirm_details))

        val addressTitle = intent.getStringExtra("addressTitle")
        branch_id = intent.getStringExtra("branch_id")!!
        user_address_id = intent.getStringExtra("user_address_id")!!

        PushDownAnim.setPushDownAnimTo(btn_checkout_note_check_out)

        if (!branch_id.isNullOrEmpty()) {
            tv_checkout_note_address_title.text =
                resources.getString(R.string.pickup_at)
        } else if (!user_address_id.isNullOrEmpty()) {
            tv_checkout_note_address_title.text =
                resources.getString(R.string.delivery_at)
        }

        tv_checkout_note_address.text = addressTitle

        val productModelList = CommonActivity.getCartProductsList(this)
        totalDiscountPrice = CommonActivity.getCartNetAmount(this)
        if (totalDiscountPrice > 0) {
            tv_checkout_note_total_item.text = productModelList.size.toString()
            tv_checkout_note_total_price_main.text = CommonActivity.getPriceWithCurrency(
                CommonActivity.getPriceFormat(
                    totalDiscountPrice.toString()
                )
            )
            btn_checkout_note_check_out.text =
                "${resources.getString(R.string.check_out)} (${CommonActivity.getPriceWithCurrency(
                    CommonActivity.getPriceFormat(
                        totalDiscountPrice.toString()
                    )
                )})"
        }

        ll_checkout_note_promo.visibility = View.GONE

        btn_checkout_note_check_out.setOnClickListener(this)
        et_checkout_note_promo_code.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_checkout_note_check_out -> {
                val note = et_checkout_note.text.toString()
                Intent(this, CheckoutActivity::class.java).apply {
                    putExtra("branch_id", branch_id)
                    putExtra("user_address_id", user_address_id)
                    putExtra("note", note)
                    if (couponModel != null) {
                        putExtra("couponModel", couponModel as Serializable)
                        putExtra("coupon_code", couponModel?.coupon_code)
                    }
                    startActivity(this)
                }
            }
            R.id.et_checkout_note_promo_code -> {
                val bottomSheetCheckoutCouponDialog =
                    BottomSheetCheckoutCouponDialog(
                        object : OnCouponSelected {
                            override fun onSelected(couponModel: CouponModel) {
                                this@CheckoutNoteActivity.couponModel = couponModel

                                et_checkout_note_promo_code.text = couponModel.coupon_code
                                ll_checkout_note_promo.visibility = View.VISIBLE

                                val couponDiscount = couponModel.discount!!.toDouble()
                                val couponMaxDiscount = couponModel.max_discount_amount!!.toDouble()

                                val finalAmount = if (couponModel.discount_type!! == "percentage") {
                                    val finalTotalDiscount =
                                        getDiscountPrice(
                                            couponDiscount.toString(),
                                            totalDiscountPrice.toString(),
                                            true,
                                            true
                                        )

                                    if (finalTotalDiscount <= couponMaxDiscount) {
                                        getDiscountPrice(
                                            couponDiscount.toString(),
                                            totalDiscountPrice.toString(),
                                            false,
                                            true
                                        )
                                    } else {
                                        couponMaxDiscount
                                    }
                                } else {
                                    couponDiscount
                                }

                                tv_checkout_note_promo_price.text =
                                    "- ${getPriceWithCurrency(
                                        getPriceFormat(
                                            finalAmount.toString()
                                        )
                                    )}"

                            }
                        })
                bottomSheetCheckoutCouponDialog.contexts = this
                val args = Bundle()
                bottomSheetCheckoutCouponDialog.arguments = args
                bottomSheetCheckoutCouponDialog.show(
                    supportFragmentManager,
                    bottomSheetCheckoutCouponDialog.tag
                )
            }
        }
    }

}