package com.foodorder.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/**
 * Created on 08-05-2020.
 */
class MarkerModel : ClusterItem {

    var mPosition: LatLng? = null
    var mTitle: String? = null
    var mSnippet: String? = null

    constructor(lat: Double, lng: Double) {
        mPosition = LatLng(lat, lng)
    }

    constructor(
        lat: Double,
        lng: Double,
        title: String?,
        snippet: String?
    ) {
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
    }

    constructor(
        mPosition: LatLng,
        title: String?,
        snippet: String?
    ) {
        this.mPosition = mPosition
        mTitle = title
        mSnippet = snippet
    }


    override fun getSnippet(): String? {
        return mSnippet
    }

    override fun getTitle(): String? {
        return mTitle
    }

    override fun getPosition(): LatLng {
        return mPosition!!
    }
}