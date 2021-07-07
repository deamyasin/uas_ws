package com.foodorder.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.foodorder.R
import com.foodorder.models.SideMenuModel
import kotlinx.android.synthetic.main.row_side_menu.view.*


class SideMenuAdapter(
    val context: Context,
    val modelList: ArrayList<SideMenuModel>,
    val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<SideMenuAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_img: ImageView = view.iv_side_menu_icon
        val tv_title: TextView = view.tv_side_menu_title
        val tv_extra: TextView = view.tv_side_menu_extra

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                val sideMenuModel = modelList[position]
                onItemClickListener.onClick(position, sideMenuModel)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_side_menu, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        holder.iv_img.setImageResource(mList.icon)
        holder.tv_title.text = mList.title
        holder.tv_extra.text = mList.extra
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    interface OnItemClickListener {
        fun onClick(position: Int, sideMenuModel: SideMenuModel)
    }

}