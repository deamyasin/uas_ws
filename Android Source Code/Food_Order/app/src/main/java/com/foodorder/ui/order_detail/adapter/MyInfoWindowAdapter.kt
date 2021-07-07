package com.foodorder.ui.order_detail.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.TextView
import com.foodorder.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

/**
 * Created on 20-08-2020.
 */
class MyInfoWindowAdapter(val context: Activity) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker?): View {
        val view = context.layoutInflater.inflate(R.layout.row_infowindow, null)

        val tvTitle = view.findViewById(R.id.tv_marker_title) as TextView
        val tvDetail = view.findViewById(R.id.tv_marker_detail) as TextView

        tvTitle.text = p0?.title
        tvDetail.text = p0?.snippet
        if (tvDetail.text.toString().isEmpty()) {
            tvDetail.visibility = View.GONE
        } else {
            tvDetail.visibility = View.VISIBLE
        }

        return view
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }
}