package com.foodorder.ui.checkout.dialog

import Config.BaseURL
import Dialogs.LoaderDialog
import Interfaces.OnCouponSelected
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.dialog_bottom_sheet_coupon.view.*
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.CouponModel
import com.foodorder.response.CommonResponse
import com.foodorder.ui.checkout.CheckoutNoteViewModel
import utils.ConnectivityReceiver
import utils.SessionManagement

/**
 * Created on 13-05-2020.
 */
class BottomSheetCheckoutCouponDialog : BottomSheetDialogFragment {

    var onCouponSelected: OnCouponSelected? = null

    lateinit var contexts: Context
    lateinit var rootView: View
    lateinit var checkoutNoteViewModel: CheckoutNoteViewModel

    constructor() : super()

    @SuppressLint("ValidFragment")
    constructor(onCouponSelected: OnCouponSelected) : super() {
        this.onCouponSelected = onCouponSelected
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation_slidebottom
        dialog!!.window!!.statusBarColor = Color.TRANSPARENT
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkoutNoteViewModel = ViewModelProvider(this)[CheckoutNoteViewModel::class.java]
        rootView = inflater.inflate(R.layout.dialog_bottom_sheet_coupon, container, false)

        rootView.ll_coupon.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = rootView.ll_coupon.height
        }

        rootView.btn_coupon_verify.setOnClickListener {

            rootView.et_coupon_code.error = null

            val code = rootView.et_coupon_code.text.toString()

            if (code.isEmpty()) {
                rootView.et_coupon_code.setError(
                    contexts.resources.getString(
                        R.string.error_field_required
                    )
                )
            } else {
                if (ConnectivityReceiver.isConnected) {
                    makeValidCoupon(code)
                } else {
                    ConnectivityReceiver.showSnackbar(contexts)
                }
            }

        }

        return rootView
    }

    private fun makeValidCoupon(couponCode: String) {
        val params = HashMap<String, String>()
        params["user_id"] = SessionManagement.UserData.getSession(contexts, BaseURL.KEY_ID)
        params["coupon_code"] = couponCode

        CommonActivity.hideShowKeyboard(
            contexts as Activity,
            rootView.et_coupon_code,
            true
        )

        val loaderDialog = LoaderDialog(contexts)
        loaderDialog.show()

        checkoutNoteViewModel.makeValidCoupon(params)
            .observe(viewLifecycleOwner, Observer { response: CommonResponse? ->
                loaderDialog.dismiss()
                if (response != null) {
                    if (response.responce!!) {
                        val gson = Gson()
                        val type = object : TypeToken<CouponModel>() {}.type
                        val couponModel = gson.fromJson<CouponModel>(
                            response.data?.toString(),
                            type
                        )
                        if (onCouponSelected != null)
                            onCouponSelected!!.onSelected(couponModel)
                        dismiss()
                    } else {
                        CommonActivity.showToast(contexts, response.message!!)
                    }
                }
            })

    }

}