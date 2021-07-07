package com.foodorder.ui.address.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.BranchModel
import kotlinx.android.synthetic.main.row_pickup.view.*


class AddressPickupAdapter(
    val context: Context,
    val modelList: ArrayList<BranchModel>,
    val onItemClickListener: OnItemClickListener?
) :
    RecyclerView.Adapter<AddressPickupAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_address: TextView = view.tv_pickup_address
        val tv_city: TextView = view.tv_pickup_city
        val tv_open_time: TextView = view.tv_pickup_open_time
        val tv_close_time: TextView = view.tv_pickup_close_time
        val tv_km: TextView = view.tv_pickup_km

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                val branchModel = modelList[position]
                onItemClickListener?.onClick(position, branchModel)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_pickup, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.tv_address.text =
            CommonActivity.getStringByLanguage(context, mList.address_en, mList.address_ar)
        holder.tv_city.text =
            CommonActivity.getStringByLanguage(context, mList.branch_name_en, mList.branch_name_ar)

        holder.tv_open_time.text =
            "${context.resources.getString(R.string.open_at)} ${CommonActivity.getConvertTime(
                mList.opening_time!!,
                1
            )}"
        holder.tv_close_time.text =
            "${context.resources.getString(R.string.close_at)} ${CommonActivity.getConvertTime(
                mList.closing_time!!,
                1
            )}"


    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    interface OnItemClickListener {
        fun onClick(position: Int, branchModel: BranchModel)
    }

}