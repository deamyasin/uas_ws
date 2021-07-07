package com.foodorder.ui.item_detail

import Config.BaseURL
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.ProductModel
import com.foodorder.models.ProductOptionModel
import com.foodorder.response.CommonResponse
import com.foodorder.ui.item_detail.adapter.ItemAddOnsAdapter
import com.foodorder.ui.login.LoginActivity
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.content_item_detail.*
import utils.ConnectivityReceiver
import utils.ContextWrapper
import utils.LanguagePrefs
import utils.SessionManagement
import java.util.*

class ItemDetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var productModel: ProductModel? = null
        var isDetailLoaded = false
        var onDetailLoadListener: OnDetailLoadListener? = null
    }

    interface OnDetailLoadListener {
        fun onLoaded()
    }

    val productOptionModelList = ArrayList<ProductOptionModel>()
    lateinit var itemAddOnsAdapter: ItemAddOnsAdapter

    lateinit var itemDetailViewModel: ItemDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonActivity.changeStatusBarColor(this, true)
        LanguagePrefs(this)
        itemDetailViewModel = ViewModelProvider(this)[ItemDetailViewModel::class.java]
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(toolbar)
        toolbar.title = ""

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

        PushDownAnim.setPushDownAnimTo(ll_item_detail_add_ons_continue_cart)

        itemAddOnsAdapter = ItemAddOnsAdapter(
            this,
            productOptionModelList,
            object : ItemAddOnsAdapter.OnItemClickListener {
                override fun onClick(position: Int, productOptionModel: ProductOptionModel) {
                    //onClick
                }

                override fun onAddClick(position: Int, productOptionModel: ProductOptionModel) {
                    if (ConnectivityReceiver.isConnected) {
                        makeAddCartItemOption(position, productOptionModel)
                    } else {
                        ConnectivityReceiver.showSnackbar(this@ItemDetailActivity)
                    }
                }

                override fun onRemoveClick(position: Int, productOptionModel: ProductOptionModel) {
                    if (ConnectivityReceiver.isConnected) {
                        makeRemoveCartItemOption(position, productOptionModel)
                    } else {
                        ConnectivityReceiver.showSnackbar(this@ItemDetailActivity)
                    }
                }
            })

        rv_item_detail_add_ons.apply {
            layoutManager = LinearLayoutManager(this@ItemDetailActivity)
            adapter = itemAddOnsAdapter
        }

        pb_item_detail_add_ons.visibility = View.GONE
        tv_item_detail_add_ons_title.visibility = View.GONE

        wv_item_detail_info.settings.javaScriptEnabled = true
        wv_item_detail_info.webChromeClient = WebChromeClient()
        wv_item_detail_info.webViewClient = WebViewClient()

        if (intent.hasExtra("productData")) {
            pb_item_detail.visibility = View.GONE
            app_bar.visibility = View.VISIBLE
            productModel = intent.getSerializableExtra("productData") as ProductModel
            updateView()
        }

        if (ConnectivityReceiver.isConnected) {
            if (intent.hasExtra("productData")) {
                makeGetProductDetail(productModel?.product_id!!)
            } else {
                pb_item_detail.visibility = View.VISIBLE
                app_bar.visibility = View.GONE
                ll_item_detail_qty.visibility = View.GONE
                makeGetProductDetail(intent.getStringExtra("product_id")!!)
            }
        } else {
            ConnectivityReceiver.showSnackbar(this)
        }

        app_bar.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                //  Collapsed
                tv_actionbar_title.setTextColor(ContextCompat.getColor(this, R.color.colorText))
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
            } else {
                //Expanded
                tv_actionbar_title.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
            }
        })

        iv_item_detail_add_ons_minus.setOnClickListener(this)
        iv_item_detail_add_ons_plus.setOnClickListener(this)
        ll_item_detail_add_ons_continue_cart.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_item_detail_add_ons_minus -> {
                minusClick()
            }
            R.id.iv_item_detail_add_ons_plus -> {
                plusClick()
            }
            R.id.ll_item_detail_add_ons_continue_cart -> {
                Intent().apply {
                    setResult(Activity.RESULT_OK, this)
                    finish()
                }
            }
        }
    }

    private fun minusClick() {
        if (SessionManagement.UserData.isLogin(this)) {
            val qty = tv_item_detail_add_ons_qty.text.toString().toInt()
            if (qty > 0) {
                CommonActivity.runBounceAnimation(
                    this,
                    iv_item_detail_add_ons_minus
                )
                if (ConnectivityReceiver.isConnected) {
                    makeRemoveCartItem()
                } else {
                    ConnectivityReceiver.showSnackbar(this)
                }
            }
        } else {
            Intent(this, LoginActivity::class.java).apply {
                putExtra("isFinish", true)
                startActivityForResult(this, 6892)
            }
        }
    }

    private fun plusClick() {
        if (SessionManagement.UserData.isLogin(this)) {
            CommonActivity.runBounceAnimation(
                this,
                iv_item_detail_add_ons_plus
            )
            if (ConnectivityReceiver.isConnected) {
                makeAddCartItem()
            } else {
                ConnectivityReceiver.showSnackbar(this)
            }
        } else {
            Intent(this, LoginActivity::class.java).apply {
                putExtra("isFinish", true)
                startActivityForResult(this, 6892)
            }
        }
    }

    private fun makeGetProductDetail(productId: String) {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["product_id"] = productId

        itemDetailViewModel.makeGetProductDetail(params)
            .observe(this, Observer { response: CommonResponse? ->
                if (response != null) {
                    if (response.responce!!) {
                        val gson = Gson()
                        val type = object : TypeToken<ProductModel>() {}.type
                        productModel = gson.fromJson<ProductModel>(
                            response.data?.toString(),
                            type
                        )

                        if (intent.hasExtra("product_id")) {
                            pb_item_detail.visibility = View.GONE
                            app_bar.visibility = View.VISIBLE
                            ll_item_detail_qty.visibility = View.VISIBLE
                        }
                        updateView()
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
                isDetailLoaded = true
                onDetailLoadListener?.onLoaded()
            })
    }

    private fun makeAddCartItem() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["product_id"] = productModel?.product_id!!
        params["qty"] = "1"

        cl_item_detail_qty.visibility = View.VISIBLE
        pb_item_detail_add_ons_qty.visibility = View.VISIBLE
        tv_item_detail_add_ons_qty.visibility = View.GONE

        itemDetailViewModel.makeAddCartItem(params)
            .observe(this, Observer { response: CommonResponse? ->
                pb_item_detail_add_ons_qty.visibility = View.GONE
                tv_item_detail_add_ons_qty.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(
                            this@ItemDetailActivity,
                            "cartData",
                            response.data!!.toString()
                        )
                        val qty = tv_item_detail_add_ons_qty.text.toString().toInt()
                        tv_item_detail_add_ons_qty.text = "${qty + 1}"

                        updatePrice()

                    } else {
                        CommonActivity.showToast(this@ItemDetailActivity, response.message!!)
                    }
                }
            })

    }

    private fun makeRemoveCartItem() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["product_id"] = productModel?.product_id!!
        params["qty"] = "1"

        cl_item_detail_qty.visibility = View.VISIBLE
        pb_item_detail_add_ons_qty.visibility = View.VISIBLE
        tv_item_detail_add_ons_qty.visibility = View.GONE

        itemDetailViewModel.makeRemoveCartItem(params)
            .observe(this, Observer { response: CommonResponse? ->
                pb_item_detail_add_ons_qty.visibility = View.GONE
                tv_item_detail_add_ons_qty.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(
                            this@ItemDetailActivity,
                            "cartData",
                            response.data!!.toString()
                        )
                        val qty = tv_item_detail_add_ons_qty.text.toString().toInt()
                        tv_item_detail_add_ons_qty.text = "${qty - 1}"

                        updatePrice()
                    } else {
                        CommonActivity.showToast(this@ItemDetailActivity, response.message!!)
                    }
                }
            })

    }

    private fun makeAddCartItemOption(position: Int, productOptionModel: ProductOptionModel) {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["product_id"] = productModel?.product_id!!
        params["product_option_id"] = productOptionModel.product_option_id!!
        params["qty"] = "1"

        productOptionModel.isLoading = true
        itemAddOnsAdapter.modelList[position] = productOptionModel
        itemAddOnsAdapter.notifyItemChanged(position)

        itemDetailViewModel.makeAddCartItemOption(params)
            .observe(this, Observer { response: CommonResponse? ->
                pb_item_detail_add_ons_qty.visibility = View.GONE
                tv_item_detail_add_ons_qty.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(
                            this@ItemDetailActivity,
                            "cartData",
                            response.data!!.toString()
                        )
                        productOptionModel.isLoading = false
                        productOptionModel.cart_qty =
                            (productOptionModel.cart_qty!!.toInt() + 1).toString()
                        itemAddOnsAdapter.modelList[position] = productOptionModel
                        itemAddOnsAdapter.notifyItemChanged(position)

                        updatePrice()
                    } else {
                        CommonActivity.showToast(this@ItemDetailActivity, response.message!!)
                    }
                }
            })

    }

    private fun makeRemoveCartItemOption(position: Int, productOptionModel: ProductOptionModel) {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["product_id"] = productModel?.product_id!!
        params["product_option_id"] = productOptionModel.product_option_id!!
        params["qty"] = "1"

        productOptionModel.isLoading = true
        itemAddOnsAdapter.modelList[position] = productOptionModel
        itemAddOnsAdapter.notifyItemChanged(position)

        itemDetailViewModel.makeRemoveCartItemOption(params)
            .observe(this, Observer { response: CommonResponse? ->
                pb_item_detail_add_ons_qty.visibility = View.GONE
                tv_item_detail_add_ons_qty.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(
                            this@ItemDetailActivity,
                            "cartData",
                            response.data!!.toString()
                        )
                        productOptionModel.isLoading = false
                        productOptionModel.cart_qty =
                            (productOptionModel.cart_qty!!.toInt() - 1).toString()
                        itemAddOnsAdapter.modelList[position] = productOptionModel
                        itemAddOnsAdapter.notifyItemChanged(position)

                        updatePrice()
                    } else {
                        CommonActivity.showToast(this@ItemDetailActivity, response.message!!)
                    }
                }
            })

    }

    private fun updatePrice() {
        val productModelCart =
            CommonActivity.getCartDetailByProductId(this, productModel?.product_id!!)
        val totalDiscountPrice = CommonActivity.getCartNetAmount(this)
        if (productModelCart != null) {
            ll_item_detail_add_ons_continue_cart.visibility = View.VISIBLE
            iv_item_detail_add_ons_minus.visibility = View.VISIBLE
            cl_item_detail_qty.visibility = View.VISIBLE
            rv_item_detail_add_ons.visibility = View.VISIBLE

            if (productModel?.optionModelList != null && productModel?.optionModelList!!.size > 0) {
                tv_item_detail_add_ons_title.visibility = View.VISIBLE
            } else {
                tv_item_detail_add_ons_title.visibility = View.GONE
            }

            val productTotal =
                productModelCart.effected_price!!.toDouble() * productModelCart.qty!!.toInt()

            tv_item_detail_add_ons_price_discount.text =
                CommonActivity.getPriceWithCurrency(
                    CommonActivity.getPriceFormat(
                        totalDiscountPrice.toString()
                    ), false
                )

            if (productModelCart.optionModelList != null && productModelCart.optionModelList.size > 0) {
                var totalOption = 0.0
                for (productOptionModel in productModelCart.optionModelList) {
                    totalOption += (productOptionModel.price!!.toDouble() * productOptionModel.qty.toInt())
                }
                tv_item_detail_add_ons_price_main.text = "+${CommonActivity.getPriceWithCurrency(
                    CommonActivity.getPriceFormat(
                        (productTotal + totalOption).toString()
                    ), true
                )}"
                /*tv_item_detail_add_ons_price_main.text =
                    "+${CommonActivity.getPriceWithCurrency(
                        CommonActivity.getPriceFormat(
                            totalOption.toString()
                        ), false
                    )}"
                tv_item_detail_add_ons_price_main.visibility = View.VISIBLE*/
            } else {
                //tv_item_detail_add_ons_price_main.visibility = View.GONE
                tv_item_detail_add_ons_price_main.text = "+${CommonActivity.getPriceWithCurrency(
                    CommonActivity.getPriceFormat(
                        productTotal.toString()
                    ), true
                )}"
            }
        } else {
            ll_item_detail_add_ons_continue_cart.visibility = View.GONE
            iv_item_detail_add_ons_minus.visibility = View.GONE
            cl_item_detail_qty.visibility = View.GONE
            tv_item_detail_add_ons_title.visibility = View.GONE
            rv_item_detail_add_ons.visibility = View.GONE
        }
    }

    private fun updateView() {
        tv_actionbar_title.text = CommonActivity.getStringByLanguage(
            this,
            productModel?.cat_name_en,
            productModel?.cat_name_ar
        )

        Glide.with(this)
            .load(BaseURL.IMG_PRODUCT_URL + productModel?.product_image)
            .placeholder(R.drawable.ic_place_holder)
            .error(R.drawable.ic_place_holder)
            .into(iv_item_detail_img)

        tv_item_detail_title.text = CommonActivity.getStringByLanguage(
            this,
            productModel?.product_name_en,
            productModel?.product_name_ar
        )
        tv_item_detail_kcal.text = productModel?.calories
        tv_item_detail_price_note.text = productModel?.price_note

        tv_item_detail_price_main.paintFlags =
            tv_item_detail_price_main.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        tv_item_detail_price_main.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(productModel?.price))
        tv_item_detail_price_discount.text =
            CommonActivity.getPriceWithCurrency(CommonActivity.getPriceFormat(productModel?.price))

        if (productModel?.is_veg == "0") {
            iv_item_detail_veg_non.setImageResource(R.drawable.xml_view_red)
            tv_item_detail_veg_non.text = resources.getString(R.string.non_veg)
        } else {
            iv_item_detail_veg_non.setImageResource(R.drawable.xml_view_green)
            tv_item_detail_veg_non.text = resources.getString(R.string.veg)
        }

        if (productModel?.is_promotional == "0") {
            iv_item_detail_promotional.visibility = View.GONE
        } else {
            iv_item_detail_promotional.visibility = View.VISIBLE
        }

        if (!productModel?.discount.isNullOrEmpty()
            && productModel?.discount!!.toDouble() > 0
        ) {
            if (productModel?.discount_type.equals("flat")) {
                tv_item_detail_price_main.visibility = View.VISIBLE
                tv_item_detail_offer.visibility = View.VISIBLE

                tv_item_detail_offer.text =
                    "${productModel?.discount} ${resources.getString(R.string.flat)}"

                tv_item_detail_price_discount.text =
                    CommonActivity.getPriceWithCurrency(
                        CommonActivity.getPriceFormat((productModel?.price!!.toDouble() - productModel?.discount!!.toDouble()).toString())
                    )
            } else if (productModel?.discount_type.equals("percentage")) {
                tv_item_detail_price_main.visibility = View.VISIBLE
                tv_item_detail_offer.visibility = View.VISIBLE

                tv_item_detail_offer.text =
                    "${productModel?.discount}% ${resources.getString(R.string.discount)
                        .replace(":", "")}"

                tv_item_detail_price_discount.text = CommonActivity.getPriceWithCurrency(
                    CommonActivity.getPriceFormat(
                        CommonActivity.getDiscountPrice(
                            productModel?.discount!!,
                            productModel?.price!!,
                            true,
                            true
                        ).toString()
                    )
                )
            } else {
                tv_item_detail_price_main.visibility = View.GONE
                tv_item_detail_offer.visibility = View.GONE
            }
        } else {
            tv_item_detail_price_main.visibility = View.GONE
            tv_item_detail_offer.visibility = View.GONE
        }

        if (productModel?.product_desc_en != null || productModel?.product_desc_ar != null) {
            Log.e(
                "TEST", CommonActivity.getStringByLanguage(
                    this,
                    productModel?.product_desc_en, productModel?.product_desc_ar
                )
            )
            val base64: String = android.util.Base64.encodeToString(
                CommonActivity.getStringByLanguage(
                    this,
                    productModel?.product_desc_en, productModel?.product_desc_ar
                )?.toByteArray(), android.util.Base64.DEFAULT
            )
            wv_item_detail_info.loadData(
                base64, "text/html; charset=UTF-8", "base64"
            )
        }

        productOptionModelList.clear()
        if (productModel?.optionModelList != null) {
            productOptionModelList.addAll(productModel?.optionModelList!!)
        }
        itemAddOnsAdapter.notifyDataSetChanged()

        CommonActivity.runLayoutAnimation(rv_item_detail_add_ons, 1)

        tv_item_detail_add_ons_qty.text =
            CommonActivity.getCartProductsQty(this, productModel?.product_id!!).toString()

        updatePrice()

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context?) {
        val newLocale = Locale(LanguagePrefs.getLang(newBase!!)!!)
        // .. create or get your new Locale object here.
        val context = ContextWrapper.wrap(newBase, newLocale)
        super.attachBaseContext(context)
    }

}
