package com.dsdelivery.ui.menu.adapter

import Config.BaseURL
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.CategoryModel
import com.foodorder.ui.menu_item.MenuItemActivity
import kotlinx.android.synthetic.main.row_menu.view.*
import utils.LanguagePrefs
import java.io.Serializable


class MenuAdapter(
    val context: Context,
    var modelList: ArrayList<CategoryModel>
) :
    RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {

    private val isRtl = LanguagePrefs.isRTl(context)

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_img: ImageView = view.iv_menu_img
        val tv_title: TextView = view.tv_menu_title
        val tv_count: TextView = view.tv_menu_count
        val linearLayout: LinearLayout = view.ll_menu

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
            .inflate(R.layout.row_menu, parent, false)
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
        holder.tv_count.text = mList.total_items

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    fun filter(models: ArrayList<CategoryModel>, query: String) {
        var query = query
        query = query.toLowerCase()

        val filteredModelList = ArrayList<CategoryModel>()
        for (model in models) {
            val text = model.cat_name_en!!.toLowerCase()
            val textAr = model.cat_name_ar!!.toLowerCase()
            if (text.contains(query) || textAr.contains(query)) {
                filteredModelList.add(model)
            }
        }
        this.modelList = filteredModelList
        notifyDataSetChanged()
    }

}