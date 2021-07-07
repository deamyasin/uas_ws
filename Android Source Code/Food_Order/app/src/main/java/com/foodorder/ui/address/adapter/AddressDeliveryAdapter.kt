package com.foodorder.ui.address.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.foodorder.R
import com.foodorder.models.AddressModel
import kotlinx.android.synthetic.main.row_delivery.view.*


class AddressDeliveryAdapter(
    val context: Context,
    val modelList: ArrayList<AddressModel>,
    val onItemClickListener: OnItemClickListener?
) :
    RecyclerView.Adapter<AddressDeliveryAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val iv_edit: ImageView = view.iv_delivery_address_edit
        val iv_delete: ImageView = view.iv_delivery_address_delete
        val tv_title: TextView = view.tv_delivery_address
        val tv_city: TextView = view.tv_delivery_address_city
        val tv_edit: TextView = view.tv_delivery_address_edit
        val tv_delete: TextView = view.tv_delivery_address_delete
        val tv_select: TextView = view.tv_delivery_address_select
        val ll_edit: LinearLayout = view.ll_delivery_edit
        val ll_delete: LinearLayout = view.ll_delivery_delete

        init {
            itemView.setOnClickListener(this)
            tv_select.setOnClickListener(this)
            ll_edit.setOnClickListener(this)
            ll_delete.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val addressModel = modelList[position]

            when (v?.id) {
                R.id.tv_delivery_address_select -> {
                    onItemClickListener?.onClick(position, addressModel)
                }
                R.id.ll_delivery_edit, R.id.iv_delivery_address_edit, R.id.tv_delivery_address_edit -> {
                    onItemClickListener?.onEditClick(position, addressModel)
                }
                R.id.ll_delivery_delete, R.id.iv_delivery_address_delete, R.id.tv_delivery_address_delete -> {
                    onItemClickListener?.onDeleteClick(position, addressModel)
                }
                else -> onItemClickListener?.onClick(position, addressModel)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_delivery, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_title.text = mList.address_line1
        holder.tv_city.text = mList.city

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    interface OnItemClickListener {
        fun onClick(position: Int, addressModel: AddressModel)
        fun onEditClick(position: Int, addressModel: AddressModel)
        fun onDeleteClick(position: Int, addressModel: AddressModel)
    }

}