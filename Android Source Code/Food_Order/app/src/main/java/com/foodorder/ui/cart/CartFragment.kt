package com.foodorder.ui.cart

import Config.BaseURL
import Dialogs.CommonAlertDialog
import Dialogs.LoaderDialog
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foodorder.ui.cart.adapter.CartAdapter
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.ProductModel
import com.foodorder.response.CommonResponse
import com.foodorder.ui.address.AddressActivity
import com.foodorder.ui.home.MainActivity
import com.foodorder.ui.item_detail.ItemDetailActivity
import com.foodorder.ui.menu.MenuFragment
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_cart.view.*
import utils.ConnectivityReceiver
import utils.SessionManagement
import java.io.Serializable

class CartFragment : Fragment() {

    companion object {
        var cartFragment: CartFragment? = null
        fun showCartDeleteDialog(context: Context) {
            CommonAlertDialog(
                context,
                context.resources.getString(R.string.cart),
                context.resources.getString(R.string.are_you_sure_you_want_to_clear_cart),
                null,
                null,
                object : CommonAlertDialog.OnClickListener {
                    override fun cancelClick() {
                        //cancelClick
                    }

                    override fun okClick() {
                        if (ConnectivityReceiver.isConnected) {
                            cartFragment?.makeClearCart()
                        } else {
                            ConnectivityReceiver.showSnackbar(context)
                        }
                    }
                }
            ).show()
        }
    }

    private val productModelList = ArrayList<ProductModel>()

    lateinit var cartAdapter: CartAdapter

    var isUp = true

    private lateinit var contexts: Context
    private lateinit var rootView: View
    private lateinit var cartFragmentViewModel: CartFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cartFragmentViewModel = ViewModelProvider(this)[CartFragmentViewModel::class.java]
        rootView = inflater.inflate(R.layout.fragment_cart, container, false)

        cartFragment = this

        PushDownAnim.setPushDownAnimTo(rootView.btn_cart_checkout)

        rootView.tv_cart_total_price_currency.text =
            SessionManagement.PermanentData.getSession(contexts, "currency")

        productModelList.clear()
        productModelList.addAll(CommonActivity.getCartProductsList(contexts))

        cartAdapter =
            CartAdapter(contexts, productModelList, object : CartAdapter.OnItemClickListener {
                override fun onClick(position: Int, productModel: ProductModel) {
                    //onClick
                }

                override fun onEditClick(position: Int, productModel: ProductModel, view: View) {
                    Intent(context, ItemDetailActivity::class.java).apply {
                        putExtra("productData", productModel as Serializable)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            view,
                            ViewCompat.getTransitionName(view)!!
                        )
                        startActivityForResult(this, 6894, options.toBundle())
                    }
                }

                override fun onDeleteClick(position: Int, productModel: ProductModel) {
                    if (ConnectivityReceiver.isConnected) {
                        makeDeleteCartItem(productModel)
                    } else {
                        ConnectivityReceiver.showSnackbar(contexts)
                    }
                }
            })

        rootView.rv_cart.apply {
            layoutManager = LinearLayoutManager(contexts)
            adapter = cartAdapter
        }

        rootView.btn_cart_checkout.setOnClickListener {
            cartFragmentViewModel.showDeliveryTypeDialog(contexts)
        }

        updatePrice()

        rootView.nsv_cart.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                if (!isUp) {
                    cartFragmentViewModel.slideUp(rootView.ll_cart_price)
                    isUp = true
                }
            } else {
                if (isUp) {
                    cartFragmentViewModel.slideDown(rootView.ll_cart_price)
                    isUp = false
                }
            }
        })

        /*rootView.rv_cart.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    if (isUp) {
                        cartFragmentViewModel.slideDown(rootView.ll_cart_price)
                        isUp = false
                    }
                } else {
                    if (!isUp) {
                        cartFragmentViewModel.slideUp(rootView.ll_cart_price)
                        isUp = true
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })*/

        rootView.ll_cart_empty.setOnClickListener {
            (contexts as MainActivity).setBottomSelection(2)
            (contexts as MainActivity).setHeaderTitle(resources.getString(R.string.menu))
            (contexts as MainActivity).setFragment(MenuFragment(), true)
        }

        return rootView
    }

    private fun updatePrice() {
        val totalDiscountPrice = CommonActivity.getCartNetAmount(contexts)
        if (totalDiscountPrice > 0) {
            rootView.ll_cart_empty.visibility = View.GONE
            rootView.btn_cart_checkout.visibility = View.VISIBLE

            rootView.tv_cart_total_price.text =
                CommonActivity.getPriceFormat(totalDiscountPrice.toString())
            MainActivity.menu_delete?.isVisible = true

            if (rootView.ll_cart_price.visibility == View.GONE) {
                cartFragmentViewModel.slideUp(rootView.ll_cart_price)
                isUp = true
            }
        } else {
            rootView.tv_cart_total_price.text = ""
            rootView.ll_cart_price.visibility = View.GONE
            rootView.ll_cart_empty.visibility = View.VISIBLE
            MainActivity.menu_delete?.isVisible = false
            isUp = false
        }
        (contexts as MainActivity).updatePrice()
    }

    private fun makeDeleteCartItem(productModel: ProductModel) {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(contexts, BaseURL.KEY_ID)
        params["product_id"] = productModel.product_id!!

        val loaderDialog = LoaderDialog(contexts)
        loaderDialog.show()

        cartFragmentViewModel.makeDeleteCartItem(params)
            .observe(viewLifecycleOwner, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(
                            contexts,
                            "cartData",
                            response.data!!.toString()
                        )

                        productModelList.clear()
                        productModelList.addAll(CommonActivity.getCartProductsList(contexts))
                        cartAdapter.notifyDataSetChanged()

                        updatePrice()
                    } else {
                        CommonActivity.showToast(contexts, response.message!!)
                    }
                }
            })
    }

    private fun makeClearCart() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(contexts, BaseURL.KEY_ID)

        val loaderDialog = LoaderDialog(contexts)
        loaderDialog.show()

        cartFragmentViewModel.makeClearCart(params)
            .observe(viewLifecycleOwner, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        SessionManagement.UserData.setSession(
                            contexts,
                            "cartData",
                            response.data!!.toString()
                        )

                        productModelList.clear()
                        cartAdapter.notifyDataSetChanged()

                        updatePrice()
                    } else {
                        CommonActivity.showToast(contexts, response.message!!)
                    }
                }
            })
    }

    fun updateCart() {
        if (::cartAdapter.isInitialized) {
            productModelList.clear()
            productModelList.addAll(CommonActivity.getCartProductsList(contexts))
            cartAdapter.notifyDataSetChanged()
            updatePrice()
        }
    }

    override fun onResume() {
        super.onResume()
        updateCart()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

}
