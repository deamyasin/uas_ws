package com.foodorder.ui.cart.dialog

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.foodorder.R
import com.foodorder.ui.address.AddressActivity
import kotlinx.android.synthetic.main.dialog_bottom_sheet_delivery_type.view.*

/**
 * Created on 13-05-2020.
 */
class BottomSheetDeliveryTypeDialog : BottomSheetDialogFragment() {

    lateinit var contexts: Context
    lateinit var rootView: View

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
        rootView = inflater.inflate(R.layout.dialog_bottom_sheet_delivery_type, container, false)

        rootView.ll_delivery_type.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = rootView.ll_delivery_type.height
        }

        rootView.btn_delivery_type_pickup.setOnClickListener {
            Intent(contexts, AddressActivity::class.java).apply {
                putExtra("isDelivery", false)
                startActivity(this)
            }
            dismiss()
        }
        rootView.btn_delivery_type_delivery.setOnClickListener {
            Intent(contexts, AddressActivity::class.java).apply {
                putExtra("isDelivery", true)
                startActivity(this)
            }
            dismiss()
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

}