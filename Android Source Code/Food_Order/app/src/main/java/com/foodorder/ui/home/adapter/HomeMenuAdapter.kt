package com.foodorder.ui.home.adapter

import Config.BaseURL
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.CategoryModel
import com.foodorder.ui.menu_item.MenuItemActivity
import kotlinx.android.synthetic.main.row_home_menu.view.*
import java.io.Serializable


class HomeMenuAdapter(
    val context: Context,
    val modelList: ArrayList<CategoryModel>,
    val onItemClickListener: OnItemClickListener?
) :
    RecyclerView.Adapter<HomeMenuAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_img: ImageView = view.iv_home_menu_img
        val tv_title: TextView = view.tv_home_menu_title

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                val categoryModel = modelList[position]
                Intent(context, MenuItemActivity::class.java).apply {
                    putExtra("categoryData", categoryModel as Serializable)
                    (context as Activity).startActivityForResult(this, 6894)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_home_menu, parent, false)
        itemView.layoutParams = ViewGroup.LayoutParams(
            (parent.width * 0.4).toInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mList = modelList[position]

        Glide.with(context)
            .load(BaseURL.IMG_CATEGORIES_URL + mList.cat_image)
            .placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ic_place_holder)
            .into(holder.iv_img)

        holder.tv_title.text =
            CommonActivity.getStringByLanguage(context, mList.cat_name_en, mList.cat_name_ar)

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    interface OnItemClickListener {
        fun onClick(position: Int, categoryModel: CategoryModel)
    }

}