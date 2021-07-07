package com.foodorder.ui.cart.adapter

import Config.BaseURL
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.ProductModel
import kotlinx.android.synthetic.main.row_cart.view.*


class CartAdapter(
    val context: Context,
    val modelList: ArrayList<ProductModel>,
    val onItemClickListener: OnItemClickListener?
) :
    RecyclerView.Adapter<CartAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val iv_img: ImageView = view.iv_cart_img
        val iv_veg_non: ImageView = view.iv_cart_veg_non
        val iv_edit: ImageView = view.iv_cart_edit
        val iv_delete: ImageView = view.iv_cart_delete
        val tv_title: TextView = view.tv_cart_title
        val tv_qty: TextView = view.tv_cart_qty
        val tv_kcal: TextView = view.tv_cart_kcal
        val tv_price: TextView = view.tv_cart_price
        val tv_add_ons: TextView = view.tv_cart_add_ons
        val tv_edit: TextView = view.tv_cart_edit
        val tv_delete: TextView = view.tv_cart_delete
        val rv_add_ons: RecyclerView = view.rv_cart_add_ons

        init {

            rv_add_ons.layoutManager = LinearLayoutManager(context)

            /*itemView.setOnClickListener {
                val position = adapterPosition
                val sideMenuModel = modelList[position]
            }*/
            iv_edit.setOnClickListener(this)
            iv_delete.setOnClickListener(this)
            tv_edit.setOnClickListener(this)
            tv_delete.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val productModel = modelList[position]

            when (v?.id) {
                R.id.iv_cart_edit, R.id.tv_cart_edit -> {
                    onItemClickListener?.onEditClick(position, productModel, iv_img)
                }
                R.id.iv_cart_delete, R.id.tv_cart_delete -> {
                    onItemClickListener?.onDeleteClick(position, productModel)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_cart, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        Glide.with(context)
            .load(BaseURL.IMG_PRODUCT_URL + mList.product_image)
            .placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ic_place_holder)
            .into(holder.iv_img)

        holder.tv_title.text = CommonActivity.getStringByLanguage(
            context,
            mList.product_name_en,
            mList.product_name_ar
        )
        holder.tv_kcal.text = mList.calories
        holder.tv_price.text = CommonActivity.getPriceWithCurrency(
            CommonActivity.getPriceFormat(mList.effected_price))
        holder.tv_qty.text = mList.qty

        if (mList.is_veg == "0") {
            holder.iv_veg_non.setImageResource(R.drawable.ic_non_veg)
        } else {
            holder.iv_veg_non.setImageResource(R.drawable.ic_veg)
        }

        if (mList.optionModelList != null && mList.optionModelList.size > 0) {
            holder.tv_add_ons.visibility = View.VISIBLE
            holder.rv_add_ons.visibility = View.VISIBLE

            holder.rv_add_ons.adapter = CartAddOnsAdapter(context, mList.optionModelList)
        } else {
            holder.tv_add_ons.visibility = View.GONE
            holder.rv_add_ons.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    interface OnItemClickListener {
        fun onClick(position: Int, productModel: ProductModel)
        fun onEditClick(position: Int, productModel: ProductModel, view: View)
        fun onDeleteClick(position: Int, productModel: ProductModel)
    }

}