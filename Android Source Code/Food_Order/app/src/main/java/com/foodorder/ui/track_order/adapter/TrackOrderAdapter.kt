package com.foodorder.ui.track_order.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.OrderModel
import com.foodorder.ui.order_detail.OrderDetailActivity
import kotlinx.android.synthetic.main.row_track_order.view.*
import java.io.Serializable


class TrackOrderAdapter(
    val context: Context,
    val modelList: ArrayList<OrderModel>
) :
    RecyclerView.Adapter<TrackOrderAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_placed: ImageView = view.iv_track_order_placed
        val iv_preparing: ImageView = view.iv_track_order_preparing
        val iv_delivery: ImageView = view.iv_track_order_delivery
        val iv_delivered: ImageView = view.iv_track_order_delivered
        val tv_id: TextView = view.tv_track_order_id
        val tv_date_time: TextView = view.tv_track_order_date_time
        val tv_price: TextView = view.tv_track_order_price
        val tv_status: TextView = view.tv_track_order_status
        val view_preparing: View = view.view_track_order_preparing
        val view_delivery: View = view.view_track_order_delivery
        val view_delivered: View = view.view_track_order_delivered
        val tv_placed: TextView = view.tv_track_order_placed
        val tv_preparing: TextView = view.tv_track_order_preparing
        val tv_delivery: TextView = view.tv_track_order_delivery
        val tv_delivered: TextView = view.tv_track_order_delivered
        val tv_placed_time: TextView = view.tv_track_order_placed_time
        val tv_preparing_time: TextView = view.tv_track_order_preparing_time
        val tv_delivery_time: TextView = view.tv_track_order_delivery_time
        val tv_delivered_time: TextView = view.tv_track_order_delivered_time
        val cardView: LinearLayout = view.cv_track_order
        private var mLastClickTime: Long = 0

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                val orderModel = modelList[position]
                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    Intent(context, OrderDetailActivity::class.java).apply {
                        putExtra("orderData", orderModel as Serializable)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            cardView,
                            ViewCompat.getTransitionName(cardView)!!
                        )
                        context.startActivity(this, options.toBundle())
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_track_order, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_id.text = mList.order_no
        holder.tv_date_time.text =
            CommonActivity.getConvertDateTime(mList.order_date!!, 1)
        holder.tv_price.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(mList.net_amount))

        holder.tv_placed_time.text = ""
        holder.tv_preparing_time.text = ""
        holder.tv_delivery_time.text = ""
        holder.tv_delivered_time.text = ""

        holder.view_preparing.isSelected = false
        holder.view_delivery.isSelected = false
        holder.view_delivered.isSelected = false

        holder.iv_placed.isSelected = false
        holder.iv_preparing.isSelected = false
        holder.iv_delivery.isSelected = false
        holder.iv_delivered.isSelected = false

        holder.tv_placed.isSelected = false
        holder.tv_preparing.isSelected = false
        holder.tv_delivery.isSelected = false
        holder.tv_delivered.isSelected = false

        holder.tv_placed_time.isSelected = true
        holder.tv_preparing_time.isSelected = true
        holder.tv_delivery_time.isSelected = true
        holder.tv_delivered_time.isSelected = true

        when (mList.status) {
            "0" -> {
                holder.tv_placed.text = context.resources.getString(R.string.order_placed)
                holder.tv_status.apply {
                    text = context.resources.getString(R.string.order_placed)
                    setTextColor(ContextCompat.getColor(context, R.color.colorCyan))
                }

                holder.iv_placed.isSelected = true
                holder.tv_placed.isSelected = true
                holder.tv_placed_time.isSelected = false
            }
            "1" -> {
                holder.tv_placed.text = context.resources.getString(R.string.order_placed)
                holder.tv_preparing.text = context.resources.getString(R.string.preparing)
                holder.tv_status.apply {
                    text = context.resources.getString(R.string.preparing)
                    setTextColor(ContextCompat.getColor(context, R.color.colorOrange))
                }

                holder.iv_placed.isSelected = true
                holder.tv_placed.isSelected = true
                holder.tv_placed_time.isSelected = false

                holder.view_preparing.isSelected = true
                holder.iv_preparing.isSelected = true
                holder.tv_preparing.isSelected = true
                holder.tv_preparing_time.isSelected = false
            }
            "2" -> {
                holder.tv_placed.text = context.resources.getString(R.string.order_placed)
                holder.tv_preparing.text = context.resources.getString(R.string.preparing)
                if (mList.order_type == "delivery") {
                    holder.tv_delivery.text = context.resources.getString(R.string.out_for_delivery)
                    holder.tv_status.text = context.resources.getString(R.string.out_for_delivery)
                } else if (mList.order_type == "pickup") {
                    holder.tv_delivery.text = context.resources.getString(R.string.ready)
                    holder.tv_status.text = context.resources.getString(R.string.ready)
                }

                holder.iv_placed.isSelected = true
                holder.tv_placed.isSelected = true
                holder.tv_placed_time.isSelected = false

                holder.view_preparing.isSelected = true
                holder.iv_preparing.isSelected = true
                holder.tv_preparing.isSelected = true
                holder.tv_preparing_time.isSelected = false

                holder.view_delivery.isSelected = true
                holder.iv_delivery.isSelected = true
                holder.tv_delivery.isSelected = true
                holder.tv_delivery_time.isSelected = false
            }
            "3" -> {
                holder.tv_placed.text = context.resources.getString(R.string.order_placed)
                holder.tv_preparing.text = context.resources.getString(R.string.preparing)
                if (mList.order_type == "delivery") {
                    holder.tv_delivery.text = context.resources.getString(R.string.out_for_delivery)
                    holder.tv_delivered.text = context.resources.getString(R.string.delivered)
                    holder.tv_status.text = context.resources.getString(R.string.delivered)
                } else if (mList.order_type == "pickup") {
                    holder.tv_delivery.text = context.resources.getString(R.string.ready)
                    holder.tv_delivered.text = context.resources.getString(R.string.picked)
                    holder.tv_status.text = context.resources.getString(R.string.picked)
                }

                holder.iv_placed.isSelected = true
                holder.tv_placed.isSelected = true
                holder.tv_placed_time.isSelected = false

                holder.view_preparing.isSelected = true
                holder.iv_preparing.isSelected = true
                holder.tv_preparing.isSelected = true
                holder.tv_preparing_time.isSelected = false

                holder.view_delivery.isSelected = true
                holder.iv_delivery.isSelected = true
                holder.tv_delivery.isSelected = true
                holder.tv_delivery_time.isSelected = false

                holder.view_delivered.isSelected = true
                holder.iv_delivered.isSelected = true
                holder.tv_delivered.isSelected = true
                holder.tv_delivered_time.isSelected = false
            }
            "5" -> {
                holder.tv_status.apply {
                    text = context.resources.getString(R.string.cancelled)
                    setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
            }
        }

        if (!mList.orderStatusModelList.isNullOrEmpty()) {
            for (orderStatusModel in mList.orderStatusModelList) {
                when (orderStatusModel.status) {
                    "0" -> {
                        holder.tv_placed_time.text =
                            CommonActivity.getConvertDateTime(mList.created_at!!, 3)
                    }
                    "1" -> {
                        holder.tv_preparing_time.text =
                            CommonActivity.getConvertDateTime(mList.created_at!!, 3)
                    }
                    "2" -> {
                        holder.tv_delivery_time.text =
                            CommonActivity.getConvertDateTime(mList.created_at!!, 3)
                    }
                    "3" -> {
                        holder.tv_delivered_time.text =
                            CommonActivity.getConvertDateTime(mList.created_at!!, 3)
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

}