package com.foodorder.ui.menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsdelivery.ui.menu.adapter.MenuAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.CategoryModel
import com.foodorder.response.CommonResponse
import com.foodorder.ui.home.MainActivity
import com.foodorder.ui.menu_item.MenuItemActivity
import kotlinx.android.synthetic.main.fragment_menu.view.*
import utils.ConnectivityReceiver

class MenuFragment : Fragment() {

    val categoryModelList = ArrayList<CategoryModel>()

    lateinit var menuAdapter: MenuAdapter

    lateinit var menuFragmentViewModel: MenuFragmentViewModel
    private lateinit var contexts: Context
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        menuFragmentViewModel = ViewModelProvider(this)[MenuFragmentViewModel::class.java]
        rootView = inflater.inflate(R.layout.fragment_menu, container, false)

        menuAdapter = MenuAdapter(contexts, categoryModelList)
        rootView.rv_menu.apply {
            layoutManager = LinearLayoutManager(contexts)
            adapter = menuAdapter
        }

        if (ConnectivityReceiver.isConnected) {
            makeGetCategoryList()
        } else {
            rootView.rv_menu.visibility = View.GONE
            ConnectivityReceiver.showSnackbar(contexts)
        }

        return rootView
    }

    private fun makeGetCategoryList() {
        rootView.pb_menu.visibility = View.VISIBLE
        rootView.rv_menu.visibility = View.GONE

        menuFragmentViewModel.makeGetCategoryList(HashMap<String, String>())
            .observe(viewLifecycleOwner, Observer { response: CommonResponse? ->
                rootView.pb_menu.visibility = View.GONE
                rootView.rv_menu.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        categoryModelList.clear()

                        val gson = Gson()
                        val type = object : TypeToken<ArrayList<CategoryModel>>() {}.type
                        categoryModelList.addAll(
                            gson.fromJson<ArrayList<CategoryModel>>(
                                response.data?.toString(),
                                type
                            )
                        )
                        menuAdapter.notifyDataSetChanged()
                        CommonActivity.runLayoutAnimation(rootView.rv_menu, 1)
                    } else {
                        CommonActivity.showToast(contexts, response.message!!)
                    }
                }
            })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

}
