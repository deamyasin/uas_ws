package com.foodorder.ui.home.fragment

import Config.BaseURL
import customViews.KKViewPager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodorder.ui.home.adapter.HomeMenuAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.BannerModel
import com.foodorder.models.CategoryModel
import com.foodorder.models.ProductModel
import com.foodorder.response.CommonResponse
import com.foodorder.ui.home.adapter.HomeMenuFragmentAdapter
import com.foodorder.ui.home.adapter.HomeOfferAdapter
import com.foodorder.ui.menu_item.adapter.MenuItemAdapter
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONObject
import utils.*

class HomeFragment : Fragment() {

    val categoryModelList = ArrayList<CategoryModel>()
    val bannerModelList = ArrayList<BannerModel>()
    val productModelList = ArrayList<ProductModel>()
    lateinit var homeMenuAdapter: HomeMenuAdapter
    lateinit var homeOfferAdapter: HomeOfferAdapter
    lateinit var menuItemAdapter: MenuItemAdapter

    lateinit var vp_banner: KKViewPager

    lateinit var rootView: View
    private lateinit var contexts: Context
    private lateinit var homeFragmentViewModel: HomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeFragmentViewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        vp_banner = rootView.vp_home_banner

        homeOfferAdapter = HomeOfferAdapter(contexts, categoryModelList)

        rootView.rv_home_offer.apply {
            visibility = View.GONE
            layoutManager = LinearLayoutManager(contexts, LinearLayoutManager.HORIZONTAL, false)
            adapter = homeOfferAdapter
        }

        menuItemAdapter = MenuItemAdapter(contexts, productModelList)

        rootView.rv_home_product.apply {
            visibility = View.GONE
            layoutManager = LinearLayoutManager(contexts)
            adapter = menuItemAdapter
        }

        rootView.tv_home_offer_title.visibility = View.GONE

        if (ConnectivityReceiver.isConnected) {
            makeGetHomeList()
        } else {
            ConnectivityReceiver.showSnackbar(contexts)
        }

        return rootView
    }

    private fun makeGetHomeList() {
        rootView.pb_home_menu.visibility = View.VISIBLE
        vp_banner.visibility = View.INVISIBLE

        val params = HashMap<String, String>()
        params["user_id"] =
            if (SessionManagement.UserData.isLogin(contexts))
                SessionManagement.UserData.getSession(contexts, BaseURL.KEY_ID)
            else
                "0"


        homeFragmentViewModel.makeGetHomeList(params)
            .observe(viewLifecycleOwner, Observer { response: CommonResponse? ->
                rootView.pb_home_menu.visibility = View.GONE
                vp_banner.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        val jsonObject = JSONObject(response.data!!.toString())
                        val bannersArray = jsonObject.getJSONArray("banners")
                        val categoriesArray = jsonObject.getJSONArray("categories")
                        val productsArray = jsonObject.getJSONArray("products")

                        categoryModelList.clear()
                        bannerModelList.clear()
                        productModelList.clear()

                        val gson = Gson()
                        val type = object : TypeToken<ArrayList<CategoryModel>>() {}.type
                        categoryModelList.addAll(
                            gson.fromJson<ArrayList<CategoryModel>>(
                                categoriesArray.toString(),
                                type
                            )
                        )
                        val typeBanner = object : TypeToken<ArrayList<BannerModel>>() {}.type
                        bannerModelList.addAll(
                            gson.fromJson<ArrayList<BannerModel>>(
                                bannersArray.toString(),
                                typeBanner
                            )
                        )
                        val typeProduct = object : TypeToken<ArrayList<ProductModel>>() {}.type
                        productModelList.addAll(
                            gson.fromJson<ArrayList<ProductModel>>(
                                productsArray.toString(),
                                typeProduct
                            )
                        )
                        bindMenu()

                        if (categoryModelList.size > 0) {
                            rootView.tv_home_offer_title.visibility = View.VISIBLE
                            rootView.rv_home_offer.visibility = View.VISIBLE
                            homeOfferAdapter.notifyDataSetChanged()
                            CommonActivity.runLayoutAnimation(rootView.rv_home_offer, 3)
                        }
                        if (productModelList.size > 0) {
                            rootView.tv_home_offer_title.visibility = View.VISIBLE
                            rootView.rv_home_product.visibility = View.VISIBLE
                            menuItemAdapter.notifyDataSetChanged()
                            CommonActivity.runLayoutAnimation(rootView.rv_home_product, 2)
                        }
                    } else {
                        CommonActivity.showToast(contexts, response.message!!)
                    }
                }
            })
    }

    private fun bindMenu() {
        val imageSliderFragmentAdapter = HomeMenuFragmentAdapter(
            childFragmentManager,
            bannerModelList,
            LanguagePrefs.getLang(contexts).equals("ar")
        )
        vp_banner.adapter = imageSliderFragmentAdapter
        vp_banner.pageMargin = 0//((rootView.width * 0.2) + 80).toInt()
        vp_banner.setPadding(150, 0, 150, 0)
        vp_banner.clipToPadding = false
        vp_banner.setAnimationEnabled(true)
        vp_banner.setFadeEnabled(false)
        vp_banner.setFadeFactor(0.6F)

        if (LanguagePrefs.getLang(contexts).equals("ar")) {
            vp_banner.post(Runnable {
                vp_banner.setCurrentItem(
                    homeFragmentViewModel.closestNumber(
                        2000,
                        categoryModelList.size
                    ) - 1, true
                )
                vp_banner.visibility = View.VISIBLE
            })
        } else {
            vp_banner.post(Runnable {
                vp_banner.setCurrentItem(
                    homeFragmentViewModel.closestNumber(
                        2000,
                        categoryModelList.size
                    ), true
                )
                vp_banner.visibility = View.VISIBLE
            })
        }

        try {
            val mScroller =
                androidx.viewpager.widget.ViewPager::class.java.getDeclaredField("mScroller")
            mScroller.isAccessible = true
            val mInterpol =
                androidx.viewpager.widget.ViewPager::class.java.getDeclaredField("sInterpolator")
            mInterpol.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

}
