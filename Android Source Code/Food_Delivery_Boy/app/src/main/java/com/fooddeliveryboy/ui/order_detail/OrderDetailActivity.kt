package com.fooddeliveryboy.ui.order_detail

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fooddeliveryboy.CommonActivity
import com.fooddeliveryboy.R
import com.fooddeliveryboy.model.OrderModel
import com.fooddeliveryboy.response.CommonResponse
import com.fooddeliveryboy.ui.order_detail.adapter.OrderItemAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.include_progressbar.*
import kotlinx.android.synthetic.main.include_progressbar.view.*
import utils.ConnectivityReceiver

class OrderDetailActivity : CommonActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orderDetailViewModel = ViewModelProvider(this).get(OrderDetailViewModel::class.java)
        setContentView(R.layout.activity_order_detail)
        setHeaderTitle(resources.getString(R.string.order_details))

        val orderModel = intent.getSerializableExtra("orderData") as OrderModel

        tv_order_detail_address.text = orderModel.address_line1
        tv_order_detail_id.text = orderModel.order_id
        tv_order_detail_date.text = CommonActivity.getConvertDate(orderModel.order_date!!, 1)
        tv_order_detail_item.text = orderModel.total_qty
        tv_order_detail_total_price.text = CommonActivity.getPriceWithCurrency(
            CommonActivity.getPriceFormat(
                orderModel.net_amount,
                true
            ), true
        )

        tv_order_detail_payment_type.text = if (orderModel.paid_by == "cod") {
            "Collect Cash"
        } else {
            "Paid"
        }

        tv_order_detail_discount.text = CommonActivity.getPriceWithCurrency(
            CommonActivity.getPriceFormat(orderModel.discount_amount, true),
            true
        )
        tv_order_detail_delivery_charge.text =
            CommonActivity.getPriceWithCurrency(
                CommonActivity.getPriceFormat(orderModel.delivery_amount, true),
                true
            )

        if (orderModel.order_type == "delivery") {
            tv_order_detail_delivery_charge.visibility = View.VISIBLE
        } else {
            tv_order_detail_delivery_charge.visibility = View.GONE
        }

        if (ConnectivityReceiver.isConnected) {
            val params = HashMap<String, String>()
            params["user_id"] = orderModel.user_id!!
            params["order_id"] = orderModel.order_id!!

            //pb_order_detail.visibility = View.VISIBLE
            circularProgressBar.visibility = View.VISIBLE
            rv_order_detail.visibility = View.GONE

            orderDetailViewModel.makeGetOrderDetail(params)
                .observe(this, Observer { response: CommonResponse? ->
                    //pb_order_detail.visibility = View.GONE
                    circularProgressBar.visibility = View.GONE
                    rv_order_detail.visibility = View.VISIBLE
                    if (response != null) {
                        if (response.responce!!) {

                            val gson = Gson()
                            val type = object : TypeToken<OrderModel>() {}.type
                            val orderModel2 =
                                gson.fromJson<OrderModel>(response.data.toString(), type)

                            rv_order_detail.apply {
                                layoutManager = LinearLayoutManager(this@OrderDetailActivity)
                                adapter =
                                    OrderItemAdapter(
                                        this@OrderDetailActivity,
                                        orderModel2.orderItemModelList!!
                                    )
                                //CommonActivity.runLayoutAnimation(this)
                            }

                        } else {
                            CommonActivity.showToast(this, response.message!!)
                        }
                    }
                })
        } else {
            //pb_order_detail.visibility = View.GONE
            circularProgressBar.visibility = View.GONE
        }

    }

}
