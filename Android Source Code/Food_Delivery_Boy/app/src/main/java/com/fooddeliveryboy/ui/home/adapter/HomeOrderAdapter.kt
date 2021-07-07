package com.fooddeliveryboy.ui.home.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.fooddeliveryboy.CommonActivity
import com.fooddeliveryboy.R
import com.fooddeliveryboy.model.OrderModel
import com.fooddeliveryboy.ui.order_detail.OrderDetailActivity
import com.fooddeliveryboy.ui.tracking.TrackingActivity
import kotlinx.android.synthetic.main.row_order.view.*
import java.io.Serializable


class HomeOrderAdapter(
    val context: Context,
    val modelList: ArrayList<OrderModel>,
    val isCompletedOrder: Boolean,
    val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<HomeOrderAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val tv_address: TextView = view.tv_order_address
        val tv_id: TextView = view.tv_order_id
        val tv_item: TextView = view.tv_order_items
        val tv_total_price: TextView = view.tv_order_total_price
        val divider: View = view.view_order_divider
        val ll_bottom: LinearLayout = view.ll_order_bottom
        val ll_location: LinearLayout = view.ll_order_location
        val ll_pickup: LinearLayout = view.ll_order_pickup
        val cardView: LinearLayout = view.cv_order

        init {

            if (isCompletedOrder) {
                divider.visibility = View.VISIBLE
                ll_bottom.visibility = View.GONE
            } else {
                divider.visibility = View.GONE
                ll_bottom.visibility = View.VISIBLE
            }

            cardView.setOnClickListener(this)
            ll_location.setOnClickListener(this)
            ll_pickup.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val orderModel = modelList[position]

            when (v?.id) {
                R.id.ll_order_location -> {
                    //onItemClickListener.onLocationClick(position, orderModel)
                    Intent(context, TrackingActivity::class.java).apply {
                        putExtra("orderData", modelList[position] as Serializable)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            cardView, "animListtodetail"
                        )
                        (context as Activity).startActivity(this, options.toBundle())
                    }
                }
                R.id.ll_order_pickup -> onItemClickListener.onPickupClick(position, orderModel)
                else -> {
                    //onItemClickListener.onClick(position, orderModel)
                    Intent(context, OrderDetailActivity::class.java).apply {
                        putExtra("orderData", modelList[position] as Serializable)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            cardView, "animListtodetail"
                        )
                        (context as Activity).startActivity(this, options.toBundle())
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_order, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_address.text = mList.address_line1
        holder.tv_id.text = mList.order_id
        holder.tv_item.text = mList.total_qty
        holder.tv_total_price.text = CommonActivity.getPriceWithCurrency(
            CommonActivity.getPriceFormat(
                mList.net_amount,
                true
            ), true
        )

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    interface OnItemClickListener {
        fun onClick(position: Int, orderModel: OrderModel)
        fun onLocationClick(position: Int, orderModel: OrderModel)
        fun onPickupClick(position: Int, orderModel: OrderModel)
    }

}