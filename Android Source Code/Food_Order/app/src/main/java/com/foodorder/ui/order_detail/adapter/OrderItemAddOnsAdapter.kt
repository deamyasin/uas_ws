package com.foodorder.ui.order_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.OrderItemOptionModel
import kotlinx.android.synthetic.main.row_cart_addons.view.*


class OrderItemAddOnsAdapter(
    val context: Context,
    val modelList: ArrayList<OrderItemOptionModel>
) :
    RecyclerView.Adapter<OrderItemAddOnsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_title: TextView = view.tv_cart_add_ons_title
        val tv_price: TextView = view.tv_cart_add_ons_price
        val tv_qty: TextView = view.tv_cart_add_ons_qty

        init {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_cart_addons, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_title.text =
            CommonActivity.getStringByLanguage(context, mList.option_name_en, mList.option_name_ar)
        holder.tv_price.text =
            "(${CommonActivity.getPriceWithCurrency(
                CommonActivity.getPriceFormat(mList.price))})"
        holder.tv_qty.text = mList.order_qty

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

}