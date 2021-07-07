package com.foodorder.ui.checkout.dialog

import Interfaces.OnCouponSelected
import Interfaces.OnEditProfileSave
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Color
import android.os.Build
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.foodorder.CommonActivity
import com.foodorder.R
import com.foodorder.models.CouponModel
import com.foodorder.ui.checkout.CheckoutActivity
import com.foodorder.ui.checkout.CheckoutNoteViewModel
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.dialog_bottom_sheet_checkout_note.view.*
import java.io.Serializable


class BottomSheetCheckoutNoteDialog : BottomSheetDialogFragment, View.OnClickListener {

    var couponModel: CouponModel? = null
    var totalDiscountPrice: Double = 0.0
    var branch_id: String = ""
    var user_address_id: String = ""

    lateinit var contexts: Context
    lateinit var rootView: View
    lateinit var checkoutNoteViewModel: CheckoutNoteViewModel

    var onEditProfileSave: OnEditProfileSave? = null

    constructor() : super()

    @SuppressLint("ValidFragment")
    constructor(onEditProfileSave: OnEditProfileSave) : super() {
        this.onEditProfileSave = onEditProfileSave
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseBottomSheetDialog)
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
        rootView = inflater.inflate(R.layout.dialog_bottom_sheet_checkout_note, container, false)

        rootView.ll_checkout_note.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = rootView.ll_checkout_note.height
        }

        val addressTitle = arguments?.getString("addressTitle")
        branch_id = arguments?.getString("branch_id")!!
        user_address_id = arguments?.getString("user_address_id")!!

        PushDownAnim.setPushDownAnimTo(rootView.btn_checkout_note_check_out)

        if (!branch_id.isNullOrEmpty()) {
            rootView.tv_checkout_note_address_title.text =
                contexts.resources.getString(R.string.pickup_at)
        } else if (!user_address_id.isNullOrEmpty()) {
            rootView.tv_checkout_note_address_title.text =
                contexts.resources.getString(R.string.delivery_at)
        }

        rootView.tv_checkout_note_address.text = addressTitle

        val productModelList = CommonActivity.getCartProductsList(contexts)
        totalDiscountPrice = CommonActivity.getCartNetAmount(contexts)
        if (totalDiscountPrice > 0) {
            rootView.tv_checkout_note_total_item.text = productModelList.size.toString()
            rootView.btn_checkout_note_check_out.text =
                "${resources.getString(R.string.check_out)} (${CommonActivity.getPriceWithCurrency(
                    CommonActivity.getPriceFormat(
                        totalDiscountPrice.toString()
                    )
                )})"
        }

        rootView.ll_checkout_note_promo.visibility = View.GONE

        rootView.btn_checkout_note_check_out.setOnClickListener(this)
        rootView.et_checkout_note_promo_code.setOnClickListener(this)

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexts = context
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_checkout_note_check_out -> {
                val note = rootView.et_checkout_note.text.toString()
                Intent(contexts, CheckoutActivity::class.java).apply {
                    putExtra("branch_id", branch_id)
                    putExtra("user_address_id", user_address_id)
                    putExtra("note", note)
                    if (couponModel != null) {
                        putExtra("couponModel", couponModel as Serializable)
                        putExtra("coupon_code", couponModel?.coupon_code)
                    }
                    contexts.startActivity(this)
                }
                dismiss()
            }
            R.id.et_checkout_note_promo_code -> {
                val bottomSheetCheckoutCouponDialog =
                    BottomSheetCheckoutCouponDialog(
                        object : OnCouponSelected {
                            override fun onSelected(couponModel: CouponModel) {
                                this@BottomSheetCheckoutNoteDialog.couponModel = couponModel

                                rootView.et_checkout_note_promo_code.text = couponModel.coupon_code
                                rootView.ll_checkout_note_promo.visibility = View.VISIBLE

                                val couponDiscount = couponModel.discount!!.toDouble()
                                val couponMaxDiscount = couponModel.max_discount_amount!!.toDouble()

                                val finalAmount = if (couponModel.discount_type!! == "percentage") {
                                    val finalTotalDiscount =
                                        CommonActivity.getDiscountPrice(
                                            couponDiscount.toString(),
                                            totalDiscountPrice.toString(),
                                            true,
                                            true
                                        )

                                    if (finalTotalDiscount <= couponMaxDiscount) {
                                        CommonActivity.getDiscountPrice(
                                            couponDiscount.toString(),
                                            totalDiscountPrice.toString(),
                                            false,
                                            true
                                        )
                                    } else {
                                        couponMaxDiscount
                                    }
                                } else {
                                    couponDiscount
                                }

                                rootView.tv_checkout_note_promo_price.text =
                                    "- ${CommonActivity.getPriceWithCurrency(
                                        CommonActivity.getPriceFormat(
                                            finalAmount.toString()
                                        )
                                    )}"
                            }
                        })
                bottomSheetCheckoutCouponDialog.contexts = contexts
                val args = Bundle()
                bottomSheetCheckoutCouponDialog.arguments = args
                bottomSheetCheckoutCouponDialog.show(
                    (contexts as FragmentActivity).supportFragmentManager,
                    bottomSheetCheckoutCouponDialog.tag
                )
            }
        }
    }

}