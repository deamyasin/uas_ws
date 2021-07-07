package com.fooddeliveryboy.ui.order_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fooddeliveryboy.CommonActivity
import com.fooddeliveryboy.R
import com.fooddeliveryboy.model.OrderItemModel
import kotlinx.android.synthetic.main.row_order_item.view.*


class OrderItemAdapter(
    val context: Context,
    val modelList: ArrayList<OrderItemModel>
) :
    RecyclerView.Adapter<OrderItemAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_title: TextView = view.tv_order_item_title
        val tv_unit: TextView = view.tv_order_item_unit
        val tv_qty: TextView = view.tv_order_item_qty
        val tv_price: TextView = view.tv_order_item_price
        val rv_option: RecyclerView = view.rv_order_item_option

        init {
            rv_option.layoutManager = LinearLayoutManager(context)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_order_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_title.text = CommonActivity.getStringByLanguage(
            context,
            mList.product_name_en,
            mList.product_name_ar
        )
        holder.tv_unit.text =
            CommonActivity.getStringByLanguage(context, mList.calories_en, mList.calories_ar)
        holder.tv_qty.text = mList.order_qty
        holder.tv_price.text = mList.product_price

        if (holder.tv_unit.text.toString().isEmpty()) {
            holder.tv_unit.visibility = View.GONE
        } else {
            holder.tv_unit.visibility = View.VISIBLE
        }

        if (mList.orderItemOptionModelList != null) {
            holder.rv_option.adapter =
                OrderItemOptionAdapter(context, mList.orderItemOptionModelList)
        }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

}