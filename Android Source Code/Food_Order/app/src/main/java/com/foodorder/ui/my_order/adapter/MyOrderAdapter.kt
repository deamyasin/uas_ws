package com.foodorder.ui.my_order.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.OrderModel
import com.foodorder.ui.order_detail.OrderDetailActivity
import kotlinx.android.synthetic.main.row_my_order.view.*
import java.io.Serializable


class MyOrderAdapter(
    val context: Context,
    val modelList: ArrayList<OrderModel>
) :
    RecyclerView.Adapter<MyOrderAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_id: TextView = view.tv_order_id
        val tv_date: TextView = view.tv_order_date
        val tv_status: TextView = view.tv_order_status
        val tv_price: TextView = view.tv_order_price

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                val orderModel = modelList[position]

                Intent(context, OrderDetailActivity::class.java).apply {
                    putExtra("position", position)
                    putExtra("orderData", orderModel as Serializable)
                    (context as Activity).startActivityForResult(this, 6894)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_my_order, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_id.text = "${context.resources.getString(R.string.order_id)} ${mList.order_no}"
        holder.tv_date.text = CommonActivity.getConvertDateTime(mList.order_date!!, 2)
        holder.tv_price.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(mList.net_amount))

        when (mList.status) {
            "0" -> {
                holder.tv_status.apply {
                    text = context.resources.getString(R.string.order_placed)
                    setTextColor(ContextCompat.getColor(context, R.color.colorCyan))
                }
            }
            "1" -> {
                holder.tv_status.apply {
                    text = context.resources.getString(R.string.preparing)
                    setTextColor(ContextCompat.getColor(context, R.color.colorOrange))
                }
            }
            "2" -> {
                holder.tv_status.apply {
                    text = if (mList.order_type == "delivery") {
                        context.resources.getString(R.string.out_for_delivery)
                    } else {
                        context.resources.getString(R.string.ready)
                    }
                    setTextColor(ContextCompat.getColor(context, R.color.colorBlue))
                }
            }
            "3" -> {
                holder.tv_status.apply {
                    text = if (mList.order_type == "delivery") {
                        context.resources.getString(R.string.delivered)
                    } else {
                        context.resources.getString(R.string.picked)
                    }
                    setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                }
            }
            "5" -> {
                holder.tv_status.apply {
                    text = context.resources.getString(R.string.cancelled)
                    setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

}