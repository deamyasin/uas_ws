package com.foodorder.ui.home.fragment

import Config.BaseURL
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.BannerModel
import com.foodorder.models.CategoryModel
import com.foodorder.ui.item_detail.ItemDetailActivity
import com.foodorder.ui.menu_item.MenuItemActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_home_menu.view.*
import java.io.Serializable

class HomeMenuFragment : Fragment() {

    lateinit var rootView: View
    private lateinit var contexts: Context

    fun newInstance(position: Int, categoryModelList: ArrayList<BannerModel>): HomeMenuFragment {
        val f = HomeMenuFragment()
        val b = Bundle()
        b.putInt("position", position)
        b.putSerializable("categoryModelList", categoryModelList)
        f.arguments = b
        return f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_home_menu, container, false)

        val position = arguments?.getInt("position")!!
        val categoryModelList =
            arguments?.getSerializable("categoryModelList") as ArrayList<BannerModel>

        val bannerModel = categoryModelList[position]

        Glide.with(contexts)
            .load(BaseURL.IMG_BANNER_URL + bannerModel.banner_image)
            .placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ic_place_holder)
            .into(rootView.iv_home_menu_img)

        rootView.ll_home_menu.setOnClickListener {
            Intent(context, ItemDetailActivity::class.java).apply {
                putExtra("product_id", bannerModel.product_id)
                (context as Activity).startActivityForResult(this, 6894)
            }
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

}
