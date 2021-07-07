package com.foodorder.ui.thanks

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.OrderModel
import com.foodorder.ui.home.MainActivity
import com.foodorder.ui.order_detail.OrderDetailActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_thanks.*
import utils.ContextWrapper
import utils.LanguagePrefs
import utils.SessionManagement
import java.io.Serializable
import java.util.*

class ThanksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguagePrefs(this)
        CommonActivity.changeStatusBarColor(this, true)
        setContentView(R.layout.activity_thanks)

        PushDownAnim.setPushDownAnimTo(btn_thanks_track_order, btn_thanks_continue)

        SessionManagement.UserData.setSession(
            this,
            "cartData",
            ""
        )

        val orderModel = intent.getSerializableExtra("orderData") as OrderModel

        tv_thanks_delivery_charge_title.text = "${resources.getString(R.string.delivery_charge)}:"

        tv_thanks_order_id.text = orderModel.order_no
        tv_thanks_order_date.text = CommonActivity.getConvertDate(orderModel.order_date!!, 6)
        tv_thanks_total_item.text = orderModel.orderItemModelList!!.size.toString()
        tv_thanks_total_price_main.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(orderModel.order_amount))
        tv_thanks_order_discount.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(orderModel.discount_amount))
        tv_thanks_gateway_charge.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(orderModel.gateway_charges))
        tv_thanks_delivery_charge.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(orderModel.delivery_amount))
        tv_thanks_total_price.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(orderModel.net_amount))

        if (orderModel.gateway_charges!!.toDouble() > 0) {
            ll_thanks_gateway_charge.visibility = View.VISIBLE
        } else {
            ll_thanks_gateway_charge.visibility = View.GONE
        }

        if (orderModel.order_type == "delivery") {
            ll_thanks_delivery_charge.visibility = View.VISIBLE
        } else if (orderModel.order_type == "pickup") {
            ll_thanks_delivery_charge.visibility = View.GONE
        }

        btn_thanks_continue.setOnClickListener {
            goNext()
        }
        btn_thanks_track_order.setOnClickListener {
            /*Intent(this, OrderDetailActivity::class.java).apply {
                putExtra("orderData", orderModel as Serializable)
                startActivityForResult(this, 6894)
                finishAffinity()
            }*/
            Intent(this, MainActivity::class.java).apply {
                putExtra("orderData", orderModel as Serializable)
                startActivity(this)
                finishAffinity()
            }
        }

    }

    override fun onBackPressed() {
        //super.onBackPressed()
        goNext()
    }

    private fun goNext() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
            finishAffinity()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        // .. create or get your new Locale object here.
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}
