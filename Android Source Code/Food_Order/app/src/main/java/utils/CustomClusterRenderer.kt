package utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.foodorder.R
import com.foodorder.models.MarkerModel

/**
 * Created by Rajesh on 2018-06-08.
 */

class CustomClusterRenderer(
    private val mContext: Context, map: GoogleMap,
    clusterManager: ClusterManager<MarkerModel>
) : DefaultClusterRenderer<MarkerModel>(mContext, map, clusterManager) {

    override fun onBeforeClusterItemRendered(
        item: MarkerModel,
        markerOptions: MarkerOptions
    ) {
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
    }

    override fun onClusterRendered(cluster: Cluster<MarkerModel>, marker: Marker) {
        super.onClusterRendered(cluster, marker)
        //marker!!.title = MapsActivity().title
    }

    override fun onClusterItemRendered(clusterItem: MarkerModel, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
    }

    override fun getColor(clusterSize: Int): Int {
        val colorCustome = ContextCompat.getColor(mContext, R.color.colorGreenLight)
        return colorCustome
    }

}
