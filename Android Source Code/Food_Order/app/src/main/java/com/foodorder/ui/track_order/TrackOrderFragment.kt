package com.foodorder.ui.track_order

import Config.BaseURL
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodorder.ui.track_order.adapter.TrackOrderAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.OrderModel
import com.foodorder.response.CommonResponse
import kotlinx.android.synthetic.main.fragment_track_order.view.*
import utils.ConnectivityReceiver
import utils.SessionManagement

class TrackOrderFragment : Fragment() {

    private val orderModelList = ArrayList<OrderModel>()

    private lateinit var trackOrderAdapter: TrackOrderAdapter

    private lateinit var rootView: View
    private lateinit var contexts: Context
    private lateinit var trackOrderFragmentViewModel: TrackOrderFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        trackOrderFragmentViewModel =
            ViewModelProvider(this)[TrackOrderFragmentViewModel::class.java]
        rootView = inflater.inflate(R.layout.fragment_track_order, container, false)

        trackOrderAdapter = TrackOrderAdapter(contexts, orderModelList)

        rootView.rv_track_order.apply {
            layoutManager = LinearLayoutManager(contexts)
            adapter = trackOrderAdapter
        }

        if (ConnectivityReceiver.isConnected) {
            makeGetTrackOrderList()
        } else {
            rootView.pb_track_order.visibility = View.GONE
            ConnectivityReceiver.showSnackbar(contexts)
        }

        return rootView
    }

    private fun makeGetTrackOrderList() {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(contexts, BaseURL.KEY_ID)

        rootView.pb_track_order.visibility = View.VISIBLE
        rootView.rv_track_order.visibility = View.GONE

        trackOrderFragmentViewModel.makeGetTrackOrderList(params).observe(viewLifecycleOwner,
            Observer { response: CommonResponse? ->
                rootView.pb_track_order.visibility = View.GONE
                rootView.rv_track_order.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        orderModelList.clear()

                        val gson = Gson()
                        val type = object : TypeToken<ArrayList<OrderModel>>() {}.type
                        orderModelList.addAll(
                            gson.fromJson<ArrayList<OrderModel>>(
                                response.data?.toString(),
                                type
                            )
                        )
                        trackOrderAdapter.notifyDataSetChanged()
                        CommonActivity.runLayoutAnimation(rootView.rv_track_order, 1)
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
