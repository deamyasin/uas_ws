package com.foodorder.ui.address

import Config.BaseURL
import Dialogs.LoaderDialog
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.AddressModel
import com.foodorder.models.BranchModel
import com.foodorder.models.MarkerModel
import com.foodorder.response.CommonResponse
import com.foodorder.ui.address.adapter.AddressDeliveryAdapter
import com.foodorder.ui.address.adapter.AddressPickupAdapter
import com.foodorder.ui.checkout.CheckoutNoteActivity
import com.foodorder.ui.choose_address.ChooseAddressActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.activity_address.*
import utils.ConnectivityReceiver
import utils.CustomClusterRenderer
import utils.SessionManagement
import java.io.Serializable


class AddressActivity : CommonActivity(), OnMapReadyCallback {

    companion object {
        lateinit var context: AddressActivity
        var googleMap: GoogleMap? = null
        var mClusterManager: ClusterManager<MarkerModel>? = null

        fun updateMarker(latLng: LatLng) {
            if (googleMap != null) {
                googleMap?.addMarker(
                    MarkerOptions().position(latLng).title("")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                )
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f))
            }
        }

        fun setMarkers(markerModelList: ArrayList<MarkerModel>) {
            mClusterManager?.clearItems()
            mClusterManager?.addItems(markerModelList)
            mClusterManager?.cluster()
            if (markerModelList.size > 0) {
                val markerModel = markerModelList[0]
                googleMap?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        markerModel.mPosition,
                        15.0f
                    )
                )
            }
        }

    }

    private val addressModelList = ArrayList<AddressModel>()
    private val branchModelList = ArrayList<BranchModel>()

    lateinit var addressPickupAdapter: AddressPickupAdapter
    lateinit var addressDeliveryAdapter: AddressDeliveryAdapter

    lateinit var addressViewModel: AddressViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addressViewModel =
            ViewModelProvider(this)[AddressViewModel::class.java]
        setContentView(R.layout.activity_address)
        setHeaderTitle(resources.getString(R.string.address))

        context = this

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_address) as SupportMapFragment
        mapFragment.getMapAsync(this)

        rv_address.layoutManager = LinearLayoutManager(this)

        if (intent.getBooleanExtra("isDelivery", false)) {
            addressDeliveryAdapter = AddressDeliveryAdapter(
                this,
                addressModelList,
                object : AddressDeliveryAdapter.OnItemClickListener {
                    override fun onClick(position: Int, addressModel: AddressModel) {
                        Intent(this@AddressActivity, CheckoutNoteActivity::class.java).apply {
                            putExtra("addressTitle", addressModel.address_line1!!)
                            putExtra("branch_id", "")
                            putExtra("user_address_id", addressModel.user_address_id!!)
                            startActivityForResult(this, 3594)
                        }
                    }

                    override fun onEditClick(position: Int, addressModel: AddressModel) {
                        Intent(this@AddressActivity, ChooseAddressActivity::class.java).apply {
                            putExtra("itemPosition", position)
                            putExtra("addressData", addressModel as Serializable)
                            startActivityForResult(this, 3594)
                        }
                    }

                    override fun onDeleteClick(position: Int, addressModel: AddressModel) {
                        if (ConnectivityReceiver.isConnected) {
                            makeDeleteAddress(position, addressModel)
                        } else {
                            ConnectivityReceiver.showSnackbar(this@AddressActivity)
                        }
                    }
                })

            rv_address.adapter = addressDeliveryAdapter

            ll_address_delivery.visibility = View.VISIBLE
            tv_address_delivery_add.setOnClickListener {
                Intent(this, ChooseAddressActivity::class.java).apply {
                    startActivityForResult(this, 3594)
                }
            }

            if (ConnectivityReceiver.isConnected) {
                makeGetAddressList()
            } else {
                pb_address.visibility = View.GONE
                ConnectivityReceiver.showSnackbar(this)
            }

        } else {
            ll_address_delivery.visibility = View.GONE

            addressPickupAdapter = AddressPickupAdapter(
                this,
                branchModelList,
                object : AddressPickupAdapter.OnItemClickListener {
                    override fun onClick(position: Int, branchModel: BranchModel) {
                        goPickupCheckout(branchModel)
                    }
                })

            rv_address.adapter = addressPickupAdapter

            if (ConnectivityReceiver.isConnected) {
                makeGetBranchList()
            } else {
                pb_address.visibility = View.VISIBLE
                ConnectivityReceiver.showSnackbar(this)
            }

        }

    }

    private fun goPickupCheckout(branchModel: BranchModel) {
        if (!branchModel.latitude.isNullOrEmpty() && !branchModel.longitude.isNullOrEmpty()) {
            Intent(this@AddressActivity, CheckoutNoteActivity::class.java).apply {
                putExtra(
                    "addressTitle", CommonActivity.getStringByLanguage(
                        this@AddressActivity,
                        branchModel.address_en,
                        branchModel.address_ar
                    )!!
                )
                putExtra("branch_id", branchModel.branch_id!!)
                putExtra("user_address_id", "")
                startActivityForResult(this, 3594)
            }
        }
    }

    private fun setUpClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = ClusterManager(
            this,
            googleMap
        )
        googleMap?.setOnCameraIdleListener(
            mClusterManager
        )
        googleMap?.setOnMarkerClickListener(
            mClusterManager
        )

        val customClusterRenderer = CustomClusterRenderer(this, googleMap!!, mClusterManager!!)
        mClusterManager?.renderer = customClusterRenderer
    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0
        setUpClusterer()
    }

    private fun makeGetBranchList() {
        pb_address.visibility = View.VISIBLE
        rv_address.visibility = View.GONE

        addressViewModel.makeGetBranchList(HashMap<String, String>())
            .observe(this, Observer { response: CommonResponse? ->
                pb_address.visibility = View.GONE
                rv_address.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        branchModelList.clear()

                        val gson = Gson()
                        val type = object : TypeToken<ArrayList<BranchModel>>() {}.type
                        branchModelList.addAll(
                            gson.fromJson<ArrayList<BranchModel>>(
                                response.data?.toString(),
                                type
                            )
                        )
                        addressPickupAdapter.notifyDataSetChanged()
                        CommonActivity.runLayoutAnimation(rv_address, 3)

                        bindMarkerPickup()
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeGetAddressList() {
        pb_address.visibility = View.VISIBLE
        rv_address.visibility = View.GONE

        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)

        addressViewModel.makeGetAddressList(params)
            .observe(this, Observer { response: CommonResponse? ->
                pb_address.visibility = View.GONE
                rv_address.visibility = View.VISIBLE
                if (response != null) {
                    if (response.responce!!) {
                        addressModelList.clear()

                        val gson = Gson()
                        val type = object : TypeToken<ArrayList<AddressModel>>() {}.type
                        addressModelList.addAll(
                            gson.fromJson<ArrayList<AddressModel>>(
                                response.data?.toString(),
                                type
                            )
                        )
                        addressDeliveryAdapter.notifyDataSetChanged()
                        runLayoutAnimation(rv_address, 1)
                        bindMarkerAddress()
                    } else {
                        showToast(this, response.message!!)
                    }
                }
            })
    }

    private fun makeDeleteAddress(position: Int, addressModel: AddressModel) {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(this, BaseURL.KEY_ID)
        params["user_address_id"] = addressModel.user_address_id!!

        val loaderDialog = LoaderDialog(this)
        loaderDialog.show()

        addressViewModel.makeDeleteAddress(params)
            .observe(this, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        addressDeliveryAdapter.modelList.removeAt(position)
                        addressDeliveryAdapter.notifyItemRemoved(position)
                        addressDeliveryAdapter.notifyItemRangeChanged(
                            0,
                            addressDeliveryAdapter.modelList.size
                        )
                    } else {
                        CommonActivity.showToast(this, response.message!!)
                    }
                }
            })
    }

    fun bindMarkerAddress() {
        val markerModelList = ArrayList<MarkerModel>()
        for (addressModel in addressModelList) {
            if (!addressModel.latitude.isNullOrEmpty() && !addressModel.longitude.isNullOrEmpty()) {
                val latLng = LatLng(
                    addressModel.latitude.toDouble(),
                    addressModel.longitude.toDouble()
                )
                markerModelList.add(
                    MarkerModel(
                        latLng,
                        addressModel.address_line1,
                        ""
                    )
                )
            }
        }
        setMarkers(markerModelList)
    }

    fun bindMarkerPickup() {
        val markerModelList = ArrayList<MarkerModel>()
        for (branchModel in branchModelList) {
            if (!branchModel.latitude.isNullOrEmpty() && !branchModel.longitude.isNullOrEmpty()) {
                val latLng = LatLng(
                    branchModel.latitude.toDouble(),
                    branchModel.longitude.toDouble()
                )
                markerModelList.add(
                    MarkerModel(
                        latLng,
                        CommonActivity.getStringByLanguage(
                            context,
                            branchModel.address_en,
                            branchModel.address_ar
                        ),
                        ""
                    )
                )
            }
        }
        setMarkers(markerModelList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3594 && resultCode == Activity.RESULT_OK) {
            val addressModel = data?.getSerializableExtra("addressData") as AddressModel
            if (data.hasExtra("itemPosition")) {
                val itemPosition = data.getIntExtra("itemPosition", 0)
                addressModelList[itemPosition] = addressModel
                addressDeliveryAdapter.notifyItemChanged(itemPosition)
            } else {
                addressModelList.add(addressModel)
                addressDeliveryAdapter.notifyDataSetChanged()
            }
            bindMarkerAddress()
        }
    }

}
