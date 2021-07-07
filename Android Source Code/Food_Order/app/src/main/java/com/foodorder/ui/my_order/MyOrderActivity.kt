package com.foodorder.ui.my_order

import Config.BaseURL
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodorder.ui.my_order.adapter.MyOrderAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.OrderModel
import com.foodorder.response.CommonResponse
import kotlinx.android.synthetic.main.activity_my_order.*
import utils.ConnectivityReceiver
import utils.SessionManagement

class MyOrderActivity : CommonActivity() {

    val orderModelList = ArrayList<OrderModel>()

    lateinit var myOrderAdapter: MyOrderAdapter

    lateinit var myOrderViewModel: MyOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myOrderViewModel = ViewModelProvider(this)[MyOrderViewModel::class.java]
        setContentView(R.layout.activity_my_order)
        setHeaderTitle(resources.getString(R.string.my_orders))
        removeToolbarShadow()

        myOrderAdapter = MyOrderAdapter(this@MyOrderActivity, orderModelList)

        rv_my_order.apply {
            layoutManager = LinearLayoutManager(this@MyOrderActivity)
            adapter = myOrderAdapter
        }

        if (ConnectivityReceiver.isConnected) {
            makeGetOrderList()
        } else {
            pb_my_order.visibility = View.GONE
            ConnectivityReceiver.showSnackbar(this)
        }

    }

    private fun makeGetOrderList() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)

        pb_my_order.visibility = View.VISIBLE
        rv_my_order.visibility = View.GONE

        myOrderViewModel.makeGetOrderList(params)
            .observe(this, Observer { response: CommonResponse? ->
                pb_my_order.visibility = View.GONE
                rv_my_order.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        orderModelList.clear()

                        val gson = Gson()
                        val type = object : TypeToken<ArrayList<OrderModel>>() {}.type
                        orderModelList.addAll(
                            gson.fromJson<ArrayList<OrderModel>>(
                                response.data?.toString(),
                                type
                            )
                        )
                        myOrderAdapter.notifyDataSetChanged()
                        CommonActivity.runLayoutAnimation(rv_my_order, 3)
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 6894 && resultCode == Activity.RESULT_OK) {
            if (data!!.hasExtra("orderData")) {
                val position = data.getIntExtra("position", 0)
                val orderModel = data.getSerializableExtra("orderData") as OrderModel
                orderModelList[position] = orderModel
                myOrderAdapter.notifyItemChanged(position)
            } else {
                Intent().apply {
                    setResult(Activity.RESULT_OK, this)
                    finish()
                }
            }
        }
    }

}
