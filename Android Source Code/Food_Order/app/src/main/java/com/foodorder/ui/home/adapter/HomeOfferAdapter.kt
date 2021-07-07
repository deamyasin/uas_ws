package com.foodorder.ui.home.adapter

import Config.BaseURL
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
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
import com.foodorder.models.BannerModel
import com.foodorder.models.CategoryModel
import com.foodorder.ui.item_detail.ItemDetailActivity
import com.foodorder.ui.menu_item.MenuItemActivity
import kotlinx.android.synthetic.main.row_offer.view.*
import utils.LanguagePrefs
import java.io.Serializable


class HomeOfferAdapter(
    val context: Context,
    val modelList: ArrayList<CategoryModel>
) :
    RecyclerView.Adapter<HomeOfferAdapter.MyViewHolder>() {

    private val isRtl = LanguagePrefs.isRTl(context)

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_img: ImageView = view.iv_offer_img
        val tv_title: TextView = view.tv_offer_title
        val linearLayout: LinearLayout = view.ll_offer

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                val categoryModel = modelList[position]

                if (!categoryModel.category_id.isNullOrEmpty()) {
                    Intent(context, MenuItemActivity::class.java).apply {
                        putExtra("categoryData", categoryModel as Serializable)
                        (context as Activity).startActivityForResult(this, 6894)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_offer, parent, false)
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
            CommonActivity.getStringByLanguage(
                context,
                mList.cat_name_en,
                mList.cat_name_ar
            )

        if (isRtl) {
            when (position) {
                0 -> {
                    holder.linearLayout.setPadding(
                        CommonActivity.dpToPx(context, 10F),
                        0,
                        CommonActivity.dpToPx(context, 20F),
                        CommonActivity.dpToPx(context, 10F)
                    )
                }
                modelList.size - 1 -> {
                    holder.linearLayout.setPadding(
                        CommonActivity.dpToPx(context, 20F),
                        0,
                        CommonActivity.dpToPx(context, 0F),
                        CommonActivity.dpToPx(context, 10F)
                    )
                }
                else -> {
                    holder.linearLayout.setPadding(
                        CommonActivity.dpToPx(context, 10F),
                        0,
                        CommonActivity.dpToPx(context, 0F),
                        CommonActivity.dpToPx(context, 10F)
                    )
                }
            }
        } else {
            when (position) {
                0 -> {
                    holder.linearLayout.setPadding(
                        CommonActivity.dpToPx(context, 20F),
                        0,
                        CommonActivity.dpToPx(context, 10F),
                        CommonActivity.dpToPx(context, 10F)
                    )
                }
                modelList.size - 1 -> {
                    holder.linearLayout.setPadding(
                        CommonActivity.dpToPx(context, 0F),
                        0,
                        CommonActivity.dpToPx(context, 20F),
                        CommonActivity.dpToPx(context, 10F)
                    )
                }
                else -> {
                    holder.linearLayout.setPadding(
                        CommonActivity.dpToPx(context, 0F),
                        0,
                        CommonActivity.dpToPx(context, 10F),
                        CommonActivity.dpToPx(context, 10F)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

}