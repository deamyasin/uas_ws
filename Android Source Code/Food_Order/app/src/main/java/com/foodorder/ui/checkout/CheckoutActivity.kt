package com.foodorder.ui.checkout

import Config.BaseURL
import Dialogs.LoaderDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.CouponModel
import com.foodorder.models.OrderModel
import com.foodorder.response.CommonResponse
import com.foodorder.ui.thanks.ThanksActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.json.JSONObject
import utils.SessionManagement
import java.io.Serializable

class CheckoutActivity : CommonActivity(), View.OnClickListener {

    var branch_id: String = ""
    var user_address_id: String = ""
    var order_note: String = ""
    var couponModel: CouponModel? = null

    lateinit var checkoutViewModel: CheckoutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutViewModel = ViewModelProvider(this)[CheckoutViewModel::class.java]
        setContentView(R.layout.activity_checkout)
        setHeaderTitle(getString(R.string.check_out))
        removeToolbarShadow()

        PushDownAnim.setPushDownAnimTo(btn_checkout_order_now)

        branch_id = intent.getStringExtra("branch_id")!!
        user_address_id = intent.getStringExtra("user_address_id")!!
        order_note = intent.getStringExtra("note")!!
        if (intent.hasExtra("couponModel")) {
            couponModel = intent.getSerializableExtra("couponModel") as CouponModel
        }

        val allowCash = SessionManagement.PermanentData.getSession(this, "enable_cod")
        val allowOnline = SessionManagement.PermanentData.getSession(this, "enable_payonline")

        ll_checkout_cod.visibility = View.GONE
        ll_checkout_cash.visibility = View.GONE

        if (allowOnline == "on") {
            ll_checkout_cash.visibility = View.VISIBLE
            ll_checkout_cod.isSelected = false
            ll_checkout_cash.isSelected = true
            iv_checkout_cod.visibility = View.GONE
            iv_checkout_cash.visibility = View.VISIBLE
        }

        if (allowCash == "on") {
            ll_checkout_cod.visibility = View.VISIBLE
            ll_checkout_cod.isSelected = true
            ll_checkout_cash.isSelected = false
            iv_checkout_cod.visibility = View.VISIBLE
            iv_checkout_cash.visibility = View.GONE
        }

        val totalMainPrice = CommonActivity.getCartTotalAmount(this)
        val totalDiscountPrice = CommonActivity.getCartNetAmount(this)

        var finalAmount: Double = totalDiscountPrice
        if (couponModel != null) {
            val couponDiscount = couponModel?.discount!!.toDouble()
            val couponMaxDiscount = couponModel?.max_discount_amount!!.toDouble()

            finalAmount = if (couponModel!!.discount_type!! == "percentage") {
                val finalTotalDiscount = CommonActivity.getDiscountPrice(
                    couponDiscount.toString(),
                    totalDiscountPrice.toString(),
                    true,
                    true
                )

                if (finalTotalDiscount <= couponMaxDiscount) {
                    finalTotalDiscount
                } else {
                    (totalDiscountPrice - couponMaxDiscount)
                }
            } else {
                (totalDiscountPrice - couponDiscount)
            }
        }
        val deliveryCharge = SessionManagement.PermanentData.getSession(
            this,
            "delivery_charge"
        )

        tv_checkout_price_main.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(totalMainPrice.toString()))
        tv_checkout_discount_code.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat((totalMainPrice - finalAmount).toString()))
        tv_checkout_delivery_charge.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(deliveryCharge))
        tv_checkout_total_price.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat((finalAmount + deliveryCharge.toDouble()).toString()))

        if (branch_id.isNotEmpty()) {
            ll_checkout_delivery.visibility = View.GONE
        } else {
            ll_checkout_delivery.visibility = View.VISIBLE
        }

        ll_checkout_cod.setOnClickListener(this)
        ll_checkout_cash.setOnClickListener(this)
        btn_checkout_order_now.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_checkout_cod -> {
                ll_checkout_cod.isSelected = true
                ll_checkout_cash.isSelected = false
                iv_checkout_cod.visibility = View.VISIBLE
                iv_checkout_cash.visibility = View.GONE
            }
            R.id.ll_checkout_cash -> {
                ll_checkout_cod.isSelected = false
                ll_checkout_cash.isSelected = true
                iv_checkout_cod.visibility = View.GONE
                iv_checkout_cash.visibility = View.VISIBLE
            }
            R.id.btn_checkout_order_now -> {
                makeSendOrder()
            }
        }
    }

    private fun makeSendOrder() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["paid_by"] = if (ll_checkout_cod.isSelected) "cod" else "online"
        params["order_type"] = if (branch_id.isNotEmpty()) "pickup" else "delivery"
        if (branch_id.isNotEmpty())
            params["branch_id"] = branch_id
        if (user_address_id.isNotEmpty())
            params["user_address_id"] = user_address_id
        params["coupon_code"] =
            if (intent.hasExtra("coupon_code")) intent.getStringExtra("coupon_code") else ""
        params["order_note"] = order_note

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        checkoutViewModel.makeSendOrder(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        checkSendOrderResponse(response)
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun checkSendOrderResponse(response: CommonResponse) {
        if (ll_checkout_cod.isSelected) {
            val gson = Gson()
            val type = object : TypeToken<OrderModel>() {}.type
            val orderModel = gson.fromJson<OrderModel>(
                response.data?.toString(),
                type
            )

            Intent(this, ThanksActivity::class.java).apply {
                putExtra("orderData", orderModel as Serializable)
                startActivity(this)
                finish()
            }
        } else {
            val jsonObject = JSONObject(response.data!!.toString())
            val paymentUrl = jsonObject.getString("responseURL")
            Intent(this, PaymentActivity::class.java).apply {
                putExtra("paymentUrl", paymentUrl)
                startActivity(this)
            }
        }
    }

}
