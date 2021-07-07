package com.foodorder.ui.home.adapter

import Config.BaseURL
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.ProductModel
import com.foodorder.ui.item_detail.ItemDetailActivity
import kotlinx.android.synthetic.main.row_home_menu_item.view.*
import java.io.Serializable


class HomeMenuItemAdapter(
    val context: Context,
    var modelList: ArrayList<ProductModel>
) :
    RecyclerView.Adapter<HomeMenuItemAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_img: ImageView = view.iv_menu_item_img
        val iv_veg_non: ImageView = view.iv_menu_item_veg_non
        val iv_promotional: ImageView = view.iv_menu_item_promotional
        val tv_offer: TextView = view.tv_menu_item_offer
        val tv_title: TextView = view.tv_menu_item_title
        val tv_veg_non: TextView = view.tv_menu_item_veg_non
        val tv_kcal: TextView = view.tv_menu_item_kcal
        val tv_price_main: TextView = view.tv_menu_item_price_main
        val tv_price_discount: TextView = view.tv_menu_item_price_discount
        val tv_price_note: TextView = view.tv_menu_item_price_note
        private var mLastClickTime: Long = 0

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                val productModel = modelList[position]

                if (SystemClock.elapsedRealtime() - mLastClickTime > 1000) {
                    Intent(context, ItemDetailActivity::class.java).apply {
                        putExtra("productData", productModel as Serializable)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            iv_img,
                            ViewCompat.getTransitionName(iv_img)!!
                        )
                        context.startActivityForResult(this, 6894, options.toBundle())
                    }
                }
                mLastClickTime = SystemClock.elapsedRealtime()

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_home_menu_item, parent, false)
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

        if (mList.price_note.isNullOrEmpty()) {
            holder.tv_price_note.visibility = View.GONE
        } else {
            holder.tv_price_note.visibility = View.VISIBLE
            holder.tv_price_note.text = mList.price_note
        }
        holder.tv_price_main.paintFlags =
            holder.tv_price_main.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.tv_price_main.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(mList.price))
        holder.tv_price_discount.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(mList.price))

        if (mList.is_veg == "0") {
            holder.iv_veg_non.setImageResource(R.drawable.xml_view_red)
            holder.tv_veg_non.text = context.resources.getString(R.string.non_veg)
        } else {
            holder.iv_veg_non.setImageResource(R.drawable.xml_view_green)
            holder.tv_veg_non.text = context.resources.getString(R.string.veg)
        }

        if (mList.is_promotional == "0") {
            holder.iv_promotional.visibility = View.GONE
        } else {
            holder.iv_promotional.visibility = View.VISIBLE
        }

        if (!mList.discount.isNullOrEmpty()
            && mList.discount.toDouble() > 0
        ) {
            if (mList.discount_type.equals("flat")) {
                holder.tv_price_main.visibility = View.VISIBLE
                holder.tv_offer.visibility = View.VISIBLE

                holder.tv_offer.text =
                    "${mList.discount} ${context.resources.getString(R.string.flat)}"

                holder.tv_price_discount.text =
                    CommonActivity.getPriceWithCurrency(
                        CommonActivity.getPriceFormat((mList.price!!.toDouble() - mList.discount!!.toDouble()).toString())
                    )
            } else if (mList.discount_type.equals("percentage")) {
                holder.tv_price_main.visibility = View.VISIBLE
                holder.tv_offer.visibility = View.VISIBLE

                holder.tv_offer.text =
                    "${mList.discount}% ${context.resources.getString(R.string.discount)
                        .replace(":", "")}"

                holder.tv_price_discount.text = CommonActivity.getPriceWithCurrency(
                    CommonActivity.getPriceFormat(
                        CommonActivity.getDiscountPrice(
                            mList.discount!!,
                            mList.price!!,
                            true,
                            true
                        ).toString()
                    )
                )
            } else {
                holder.tv_price_main.visibility = View.GONE
                holder.tv_offer.visibility = View.GONE
            }
        } else {
            holder.tv_price_main.visibility = View.GONE
            holder.tv_offer.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    fun filter(models: ArrayList<ProductModel>, query: String) {
        var query = query
        query = query.toLowerCase()

        val filteredModelList = ArrayList<ProductModel>()
        for (model in models) {
            val text = model.product_name_en!!.toLowerCase()
            val textAr = model.product_name_ar!!.toLowerCase()
            val price = model.price!!.toLowerCase()
            if (text.contains(query)
                || textAr.contains(query)
                || price.contains(query)
            ) {
                filteredModelList.add(model)
            }
        }
        this.modelList = filteredModelList
        notifyDataSetChanged()
    }

}