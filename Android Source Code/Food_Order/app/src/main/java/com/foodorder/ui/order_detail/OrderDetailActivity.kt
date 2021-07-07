package com.foodorder.ui.order_detail

import Config.BaseURL
import Dialogs.LoaderDialog
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.OrderItemModel
import com.foodorder.models.OrderModel
import com.foodorder.response.CommonResponse
import com.foodorder.ui.order_detail.adapter.OrderItemAdapter
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.activity_thanks.*
import utils.ConnectivityReceiver
import utils.SessionManagement
import java.io.Serializable

class OrderDetailActivity : CommonActivity(), View.OnClickListener {

    val orderItemModel = ArrayList<OrderItemModel>()

    lateinit var orderItemAdapter: OrderItemAdapter

    lateinit var orderModel: OrderModel

    lateinit var orderDetailViewModel: OrderDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderDetailViewModel = ViewModelProvider(this)[OrderDetailViewModel::class.java]
        setContentView(R.layout.activity_order_detail)
        setHeaderTitle(resources.getString(R.string.order_detail))

        orderModel = intent.getSerializableExtra("orderData") as OrderModel

        PushDownAnim.setPushDownAnimTo(tv_order_detail_cancel, tv_order_detail_resend)

        orderItemAdapter = OrderItemAdapter(this, orderItemModel)

        rv_order_detail_item.apply {
            layoutManager = LinearLayoutManager(this@OrderDetailActivity)
            adapter = orderItemAdapter
        }

        tv_order_detail_id.text = orderModel.order_no
        tv_order_detail_date_time.text =
            CommonActivity.getConvertDateTime(orderModel.order_date!!, 1)
        tv_order_detail_price_currency.text =
            SessionManagement.PermanentData.getSession(this, "currency")
        tv_order_detail_price_main.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(orderModel.order_amount))
        tv_order_detail_delivery_charge.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(orderModel.delivery_amount))
        tv_order_detail_discount_code.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(orderModel.discount_amount))
        tv_order_detail_price.text = CommonActivity.getPriceFormat(orderModel.net_amount)
        tv_order_detail_price_top.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(orderModel.net_amount))

        if (orderModel.status == "0" || orderModel.status == "1") {
            tv_order_detail_cancel.visibility = View.VISIBLE
        } else {
            tv_order_detail_cancel.visibility = View.GONE
        }

        updateOrderStatus()

        if (ConnectivityReceiver.isConnected) {
            makeGetOrderDetail()
        } else {
            pb_order_detail.visibility = View.GONE
            ConnectivityReceiver.showSnackbar(this)
        }

        tv_order_detail_cancel.setOnClickListener(this)
        tv_order_detail_resend.setOnClickListener(this)
        tv_order_detail_track_order.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_order_detail_cancel -> {
                if (ConnectivityReceiver.isConnected) {
                    makeCancelOrder()
                } else {
                    ConnectivityReceiver.showSnackbar(this)
                }
            }
            R.id.tv_order_detail_resend -> {
                resendClick()
            }
            R.id.tv_order_detail_track_order -> {
                Intent(this, OrderTrackMapActivity::class.java).apply {
                    putExtra("orderData", orderModel as Serializable)
                    startActivity(this)
                }
            }
        }
    }

    private fun resendClick() {
        if (CommonActivity.getCartProductsList(this).size > 0) {
            orderDetailViewModel.reorderAlertLiveData.observe(this, Observer { isOk ->
                if (isOk) {
                    if (ConnectivityReceiver.isConnected) {
                        makeReOrder()
                    } else {
                        ConnectivityReceiver.showSnackbar(this)
                    }
                }
            })
            orderDetailViewModel.showReorderAlert(this)
        } else {
            if (ConnectivityReceiver.isConnected) {
                makeReOrder()
            } else {
                ConnectivityReceiver.showSnackbar(this)
            }
        }
    }

    private fun makeGetOrderDetail() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["order_id"] = orderModel.order_id!!

        pb_order_detail.visibility = View.VISIBLE
        rv_order_detail_item.visibility = View.GONE

        orderDetailViewModel.makeGetOrderDetail(params)
            .observe(this, Observer { response: CommonResponse? ->
                pb_order_detail.visibility = View.GONE
                rv_order_detail_item.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        val gson = Gson()
                        val type = object : TypeToken<OrderModel>() {}.type
                        orderModel = gson.fromJson<OrderModel>(
                            response.data?.toString(),
                            type
                        )
                        if (orderModel.orderItemModelList != null && orderModel.orderItemModelList!!.size > 0) {
                            orderItemModel.clear()
                            orderItemModel.addAll(orderModel.orderItemModelList!!)
                            orderItemAdapter.notifyDataSetChanged()
                            CommonActivity.runLayoutAnimation(rv_order_detail_item, 1)
                        }

                        updateOrderStatus()
                    } else {
                        showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeReOrder() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["order_id"] = orderModel.order_id!!

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        orderDetailViewModel.makeReOrder(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(
                            this,
                            "cartData",
                            response.data!!.toString()
                        )
                        Intent().apply {
                            setResult(Activity.RESULT_OK, this)
                            finish()
                        }
                    } else {
                        showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeCancelOrder() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["order_id"] = orderModel.order_id!!

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        orderDetailViewModel.makeCancelOrder(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        orderModel.status = "5"
                        Intent().apply {
                            setResult(Activity.RESULT_OK, this)
                            putExtra("position", intent.getIntExtra("position", 0))
                            putExtra("orderData", orderModel as Serializable)
                            finish()
                        }
                    } else {
                        showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun updateOrderStatus() {

        tv_order_detail_track_order.visibility = View.GONE

        tv_order_detail_placed.text = ""
        tv_order_detail_preparing.text = ""
        tv_order_detail_delivery.text = ""
        tv_order_detail_delivered.text = ""

        view_order_detail_preparing.isSelected = false
        view_order_detail_preparing2.isSelected = false
        view_order_detail_delivery.isSelected = false
        view_order_detail_delivery2.isSelected = false
        view_order_detail_delivered.isSelected = false
        view_order_detail_delivered2.isSelected = false

        iv_order_detail_placed.isSelected = false
        iv_order_detail_preparing.isSelected = false
        iv_order_detail_delivery.isSelected = false
        iv_order_detail_delivered.isSelected = false

        tv_order_detail_placed.isSelected = true
        tv_order_detail_preparing.isSelected = true
        tv_order_detail_delivery.isSelected = true
        tv_order_detail_delivered.isSelected = true

        tv_order_detail_placed_time.isSelected = true
        tv_order_detail_preparing_time.isSelected = true
        tv_order_detail_delivery_time.isSelected = true
        tv_order_detail_delivered_time.isSelected = true

        when (orderModel.status) {
            "0" -> {
                tv_order_detail_placed.text = resources.getString(R.string.order_placed)
                tv_order_detail_status.apply {
                    text = resources.getString(R.string.order_placed)
                    setTextColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorCyan
                        )
                    )
                }

                iv_order_detail_placed.isSelected = true
                tv_order_detail_placed.isSelected = false
                tv_order_detail_placed_time.isSelected = false
            }
            "1" -> {
                tv_order_detail_placed.text = resources.getString(R.string.order_placed)
                tv_order_detail_preparing.text = resources.getString(R.string.preparing)
                tv_order_detail_status.apply {
                    text = resources.getString(R.string.preparing)
                    setTextColor(
                        ContextCompat.getColor(
                            this@OrderDetailActivity,
                            R.color.colorOrange
                        )
                    )
                }

                iv_order_detail_placed.isSelected = true
                tv_order_detail_placed.isSelected = false
                tv_order_detail_placed_time.isSelected = false

                view_order_detail_preparing.isSelected = true
                view_order_detail_preparing2.isSelected = true
                iv_order_detail_preparing.isSelected = true
                tv_order_detail_preparing.isSelected = false
                tv_order_detail_preparing_time.isSelected = false
            }
            "2" -> {
                tv_order_detail_placed.text = resources.getString(R.string.order_placed)
                tv_order_detail_preparing.text = resources.getString(R.string.preparing)
                if (orderModel.order_type == "delivery") {
                    tv_order_detail_delivery.text = resources.getString(R.string.out_for_delivery)
                    tv_order_detail_track_order.visibility = View.VISIBLE
                    tv_order_detail_status.text = resources.getString(R.string.out_for_delivery)
                } else if (orderModel.order_type == "pickup") {
                    tv_order_detail_delivery.text = resources.getString(R.string.ready)
                    tv_order_detail_track_order.visibility = View.VISIBLE
                    tv_order_detail_status.text = resources.getString(R.string.ready)
                }
                tv_order_detail_status.setTextColor(
                    ContextCompat.getColor(
                        this@OrderDetailActivity,
                        R.color.colorBlue
                    )
                )

                iv_order_detail_placed.isSelected = true
                tv_order_detail_placed.isSelected = false
                tv_order_detail_placed_time.isSelected = false

                view_order_detail_preparing.isSelected = true
                view_order_detail_preparing2.isSelected = true
                iv_order_detail_preparing.isSelected = true
                tv_order_detail_preparing.isSelected = false
                tv_order_detail_preparing_time.isSelected = false

                view_order_detail_delivery.isSelected = true
                view_order_detail_delivery2.isSelected = true
                iv_order_detail_delivery.isSelected = true
                tv_order_detail_delivery.isSelected = false
                tv_order_detail_delivery_time.isSelected = false
            }
            "3" -> {
                tv_order_detail_placed.text = resources.getString(R.string.order_placed)
                tv_order_detail_preparing.text = resources.getString(R.string.preparing)
                if (orderModel.order_type == "delivery") {
                    tv_order_detail_delivery.text = resources.getString(R.string.out_for_delivery)
                    tv_order_detail_delivered.text = resources.getString(R.string.delivered)
                    tv_order_detail_status.text = resources.getString(R.string.delivered)
                } else if (orderModel.order_type == "pickup") {
                    tv_order_detail_delivery.text = resources.getString(R.string.ready)
                    tv_order_detail_delivered.text = resources.getString(R.string.picked)
                    tv_order_detail_status.text = resources.getString(R.string.picked)
                }
                tv_order_detail_status.setTextColor(
                    ContextCompat.getColor(
                        this@OrderDetailActivity,
                        R.color.colorGreen
                    )
                )

                iv_order_detail_placed.isSelected = true
                tv_order_detail_placed.isSelected = false
                tv_order_detail_placed_time.isSelected = false

                view_order_detail_preparing.isSelected = true
                view_order_detail_preparing2.isSelected = true
                iv_order_detail_preparing.isSelected = true
                tv_order_detail_preparing.isSelected = false
                tv_order_detail_preparing_time.isSelected = false

                view_order_detail_delivery.isSelected = true
                view_order_detail_delivery2.isSelected = true
                iv_order_detail_delivery.isSelected = true
                tv_order_detail_delivery.isSelected = false
                tv_order_detail_delivery_time.isSelected = false

                view_order_detail_delivered.isSelected = true
                view_order_detail_delivered2.isSelected = true
                iv_order_detail_delivered.isSelected = true
                tv_order_detail_delivered.isSelected = false
                tv_order_detail_delivered_time.isSelected = false
            }
            "5" -> {
                tv_order_detail_status.apply {
                    text = resources.getString(R.string.cancelled)
                    setTextColor(ContextCompat.getColor(this@OrderDetailActivity, R.color.colorRed))
                }
            }
        }

        if (orderModel.orderStatusModelList != null
            && orderModel.orderStatusModelList!!.size > 0
        ) {
            for (orderStatusModel in orderModel.orderStatusModelList!!) {
                when (orderStatusModel.status) {
                    "0" -> {
                        tv_order_detail_placed_time.text =
                            CommonActivity.getConvertDateTime(orderModel.created_at!!, 3)
                    }
                    "1" -> {
                        tv_order_detail_preparing_time.text =
                            CommonActivity.getConvertDateTime(orderModel.created_at!!, 3)
                    }
                    "2" -> {
                        tv_order_detail_delivery_time.text =
                            CommonActivity.getConvertDateTime(orderModel.created_at!!, 3)
                    }
                    "3" -> {
                        tv_order_detail_delivered_time.text =
                            CommonActivity.getConvertDateTime(orderModel.created_at!!, 3)
                    }
                }
            }
        }

    }

}
