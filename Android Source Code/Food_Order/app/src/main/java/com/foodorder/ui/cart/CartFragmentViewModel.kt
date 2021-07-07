package com.foodorder.ui.cart

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse
import com.foodorder.ui.cart.dialog.BottomSheetDeliveryTypeDialog
import com.foodorder.ui.checkout.dialog.BottomSheetCheckoutNoteDialog
import utils.MyBounceInterpolator

/**
 * Created on 07-05-2020.
 */
class CartFragmentViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeDeleteCartItem(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.deleteCartItem(params)
    }

    fun makeClearCart(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.cleanCart(params)
    }

    fun showDeliveryTypeDialog(context: Context) {
        val bottomSheetDeliveryTypeDialog =
            BottomSheetDeliveryTypeDialog()
        bottomSheetDeliveryTypeDialog.contexts = context
        if (bottomSheetDeliveryTypeDialog.isVisible) {
            bottomSheetDeliveryTypeDialog.dismiss()
        } else {
            val args = Bundle()
            bottomSheetDeliveryTypeDialog.arguments = args
            bottomSheetDeliveryTypeDialog.show(
                (context as FragmentActivity).supportFragmentManager,
                bottomSheetDeliveryTypeDialog.tag
            )
        }
    }

    // slide the view from below itself to the current position
    fun slideUp(view: View) {
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            0F,  // fromYDelta
            -view.height.toFloat()
        ) // toYDelta
        animate.duration = 200
        animate.fillAfter = false
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                //onAnimationRepeat
            }

            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }

            override fun onAnimationStart(animation: Animation?) {
                //onAnimationStart
            }
        })
        view.startAnimation(animate)
    }

    // slide the view from its current position to below itself
    fun slideDown(view: View) {
        view.visibility = View.VISIBLE
        val interpolator = MyBounceInterpolator(0.1, 15.0)
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            view.height.toFloat(),  // fromYDelta
            0F
        ) // toYDelta
        animate.interpolator = interpolator
        animate.duration = 300
        animate.fillAfter = true
        view.startAnimation(animate)

    }

}