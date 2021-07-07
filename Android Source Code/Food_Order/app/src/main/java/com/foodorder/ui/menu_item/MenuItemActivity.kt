package com.foodorder.ui.menu_item

import Config.BaseURL
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodorder.ui.menu_item.adapter.MenuItemAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.CategoryModel
import com.foodorder.models.ProductModel
import com.foodorder.response.CommonResponse
import com.foodorder.ui.cart.CartFragment
import com.foodorder.ui.home.MainActivity
import kotlinx.android.synthetic.main.activity_menu_item.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.*
import utils.ConnectivityReceiver
import utils.SessionManagement

class MenuItemActivity : CommonActivity() {

    val productModelList = ArrayList<ProductModel>()
    lateinit var menuItemAdapter: MenuItemAdapter

    var isSearch: Boolean = false

    lateinit var categoryModel: CategoryModel

    lateinit var menuItemFragmentViewModel: MenuItemFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menuItemFragmentViewModel = ViewModelProvider(this)[MenuItemFragmentViewModel::class.java]
        setContentView(R.layout.activity_menu_item)

        menuItemAdapter = MenuItemAdapter(this@MenuItemActivity, productModelList)

        rv_menu_item.apply {
            layoutManager = LinearLayoutManager(this@MenuItemActivity)
            adapter = menuItemAdapter
        }

        when {
            intent.hasExtra("categoryData") -> {
                isSearch = false
                categoryModel = intent.getSerializableExtra("categoryData") as CategoryModel
                setHeaderTitle(
                    CommonActivity.getStringByLanguage(
                        this,
                        categoryModel.cat_name_en,
                        categoryModel.cat_name_ar
                    )!!
                )

                et_menu_item_search.visibility = View.GONE

                loadCategory(false)
            }
            intent.hasExtra("discount") -> {
                isSearch = false
                setHeaderTitle(resources.getString(R.string.app_name))

                et_menu_item_search.visibility = View.GONE

                loadCategory(true)
            }
            else -> {
                isSearch = true
                setHeaderTitle(resources.getString(R.string.search))

                pb_menu_item.visibility = View.GONE
                et_menu_item_search.visibility = View.VISIBLE
                et_menu_item_search.requestFocus()
                CommonActivity.hideShowKeyboard(this, et_menu_item_search, false)
            }
        }

        var job: Job? = null
        et_menu_item_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //afterTextChanged
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //beforeTextChanged
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length > 1) {
                    GlobalScope.launch(Dispatchers.IO) {
                        job?.cancel()
                        job = launch(Dispatchers.IO) {
                            //handle before delay
                            delay(500)
                            //handle after delay
                            launch(Dispatchers.Main) {
                                if (ConnectivityReceiver.isConnected) {
                                    makeGetSearchProductList(s.toString())
                                } else {
                                    rv_menu_item.visibility = View.GONE
                                    ConnectivityReceiver.showSnackbar(this@MenuItemActivity)
                                }
                            }
                        }
                    }
                } else {
                    job?.cancel()
                    pb_menu_item.visibility = View.GONE
                    rv_menu_item.visibility = View.GONE
                    productModelList.clear()
                }
            }
        })

    }

    private fun loadCategory(discount: Boolean) {
        if (ConnectivityReceiver.isConnected) {
            makeGetCategoryList(discount)
        } else {
            rv_menu_item.visibility = View.GONE
            ConnectivityReceiver.showSnackbar(this)
        }
    }

    private fun makeGetCategoryList(discount: Boolean) {
        pb_menu_item.visibility = View.VISIBLE
        rv_menu_item.visibility = View.GONE

        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        if (::categoryModel.isInitialized) {
            params["category_id"] = categoryModel.category_id!!
        }
        params["discount"] = discount.toString()

        menuItemFragmentViewModel.makeGetProductList(params)
            .observe(this, Observer { response: CommonResponse? ->
                pb_menu_item.visibility = View.GONE
                rv_menu_item.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        productModelList.clear()

                        val gson = Gson()
                        val type = object : TypeToken<ArrayList<ProductModel>>() {}.type
                        productModelList.addAll(
                            gson.fromJson<ArrayList<ProductModel>>(
                                response.data?.toString(),
                                type
                            )
                        )
                        CommonActivity.runLayoutAnimation(rv_menu_item, 2)
                    } else {
                        showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeGetSearchProductList(searchText: String) {
        pb_menu_item.visibility = View.VISIBLE
        rv_menu_item.visibility = View.GONE

        val params = HashMap<String, String>()
        if (SessionManagement.UserData.isLogin(this)) {
            params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        } else {
            params["user_id"] = "0"
        }
        params["search"] = searchText

        et_menu_item_search.clearFocus()
        CommonActivity.hideShowKeyboard(this, et_menu_item_search, true)

        menuItemFragmentViewModel.makeGetSearchProductList(params)
            .observe(this, Observer { response: CommonResponse? ->
                pb_menu_item.visibility = View.GONE
                rv_menu_item.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        productModelList.clear()

                        val gson = Gson()
                        val type = object : TypeToken<ArrayList<ProductModel>>() {}.type
                        productModelList.addAll(
                            gson.fromJson<ArrayList<ProductModel>>(
                                response.data?.toString(),
                                type
                            )
                        )
                        CommonActivity.runLayoutAnimation(rv_menu_item, 2)
                    } else {
                        showToast(this, response.message!!)
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 6894 && resultCode == Activity.RESULT_OK) {
            Intent().apply {
                setResult(Activity.RESULT_OK, this)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        val menuDelete = menu.findItem(R.id.action_delete)
        menuDelete.icon =
            ContextCompat.getDrawable(this@MenuItemActivity, R.drawable.ic_search_green)
        menuDelete.isVisible = false
        GlobalScope.launch(Dispatchers.Main) {
            delay(300)
            menuDelete.apply {
                isVisible = !isSearch
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        } else if (item!!.itemId == R.id.action_delete) {
            Intent(this, MenuItemActivity::class.java).apply {
                startActivityForResult(this, 6894)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
