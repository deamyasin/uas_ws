package com.foodorder.ui.item_detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.ProductOptionModel
import kotlinx.android.synthetic.main.row_item_add_ons.view.*


class ItemAddOnsAdapter(
    val context: Context,
    val modelList: ArrayList<ProductOptionModel>,
    val onItemClickListener: OnItemClickListener?
) :
    RecyclerView.Adapter<ItemAddOnsAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val iv_minus: ImageView = view.iv_item_add_ons_minus
        val iv_plus: ImageView = view.iv_item_add_ons_plus
        val tv_title: TextView = view.tv_item_add_ons_title
        val tv_price: TextView = view.tv_item_add_ons_price
        val tv_qty: TextView = view.tv_item_add_ons_qty
        val pb_qty: ProgressBar = view.pb_item_add_ons_qty
        val cl_qty: ConstraintLayout = view.cl_item_add_ons_qty

        init {
            iv_minus.setOnClickListener(this)
            iv_plus.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val productOptionModel = modelList[position]

            when (v?.id) {
                R.id.iv_item_add_ons_minus -> {
                    val qty = tv_qty.text.toString().toInt()
                    if (qty > 0) {
                        //tv_qty.text = "${qty - 1}"
                        CommonActivity.runBounceAnimation(context, iv_minus)
                        onItemClickListener?.onRemoveClick(position, productOptionModel)
                    }
                }
                R.id.iv_item_add_ons_plus -> {
                    val qty = tv_qty.text.toString().toInt()
                    //tv_qty.text = "${qty + 1}"
                    CommonActivity.runBounceAnimation(context, iv_plus)
                    onItemClickListener?.onAddClick(position, productOptionModel)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item_add_ons, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_title.text =
            CommonActivity.getStringByLanguage(context, mList.option_name_en, mList.option_name_ar)
        holder.tv_price.text = CommonActivity.getPriceWithCurrency(
            CommonActivity.getPriceFormat(mList.price)
        )
        holder.tv_qty.text = mList.cart_qty

        if (mList.multiple == "0") {
            holder.tv_qty.visibility = View.GONE
            holder.pb_qty.visibility = View.GONE
            holder.cl_qty.visibility = View.GONE
            holder.iv_minus.setImageResource(R.mipmap.ic_checkbox_check)
            holder.iv_plus.setImageResource(R.mipmap.ic_checkbox_uncheck)
            if (mList.isLoading) {
                holder.cl_qty.visibility = View.VISIBLE
                holder.pb_qty.visibility = View.VISIBLE
                holder.iv_minus.visibility = View.GONE
                holder.iv_plus.visibility = View.GONE
            } else {
                holder.cl_qty.visibility = View.GONE
                holder.pb_qty.visibility = View.GONE
                if (mList.cart_qty.toInt() > 0) {
                    holder.iv_minus.visibility = View.VISIBLE
                    holder.iv_plus.visibility = View.GONE
                } else {
                    holder.iv_plus.visibility = View.VISIBLE
                    holder.iv_minus.visibility = View.GONE
                }
            }
        } else {
            holder.iv_minus.visibility = View.VISIBLE
            holder.iv_plus.visibility = View.VISIBLE
            holder.tv_qty.visibility = View.VISIBLE
            holder.pb_qty.visibility = View.VISIBLE
            holder.cl_qty.visibility = View.VISIBLE
            holder.iv_minus.setImageResource(R.mipmap.ic_minus)
            holder.iv_plus.setImageResource(R.mipmap.ic_plus)

            if (mList.isLoading) {
                holder.pb_qty.visibility = View.VISIBLE
                holder.tv_qty.visibility = View.GONE
            } else {
                holder.pb_qty.visibility = View.GONE
                holder.tv_qty.visibility = View.VISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    interface OnItemClickListener {
        fun onClick(position: Int, productOptionModel: ProductOptionModel)
        fun onAddClick(position: Int, productOptionModel: ProductOptionModel)
        fun onRemoveClick(position: Int, productOptionModel: ProductOptionModel)
    }

}