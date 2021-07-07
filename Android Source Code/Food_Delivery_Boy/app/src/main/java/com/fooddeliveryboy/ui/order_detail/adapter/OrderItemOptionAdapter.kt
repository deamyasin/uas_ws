package com.fooddeliveryboy.ui.order_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fooddeliveryboy.CommonActivity
import com.fooddeliveryboy.R
import com.fooddeliveryboy.model.OrderItemOptionModel
import kotlinx.android.synthetic.main.row_order_item_option.view.*


class OrderItemOptionAdapter(
    val context: Context,
    val modelList: ArrayList<OrderItemOptionModel>
) :
    RecyclerView.Adapter<OrderItemOptionAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_title: TextView = view.tv_order_item_option_title
        val tv_unit: TextView = view.tv_order_item_option_unit
        val tv_qty: TextView = view.tv_order_item_option_qty
        val tv_price: TextView = view.tv_order_item_option_price

        init {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_order_item_option, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_title.text = CommonActivity.getStringByLanguage(
            context,
            mList.option_name_en,
            mList.option_name_ar
        )
        holder.tv_qty.text = mList.order_qty
        holder.tv_price.text = mList.option_price

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

}