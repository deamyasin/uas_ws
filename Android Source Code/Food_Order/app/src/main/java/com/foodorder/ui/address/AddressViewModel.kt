package com.foodorder.ui.address

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse
import com.foodorder.ui.checkout.dialog.BottomSheetCheckoutNoteDialog

/**
 * Created on 06-05-2020.
 */
class AddressViewModel : ViewModel() {
    val projectRepository = ProjectRepository()

    fun makeGetAddressList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getAddressList(params)
    }

    fun makeDeleteAddress(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.deleteAddress(params)
    }

    fun makeGetBranchList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getBranchList(params)
    }

    fun showCheckoutNoteDialog(context: Context, addressTitle: String, userAddressId: String) {
        val bottomSheetCheckoutNoteDialog =
            BottomSheetCheckoutNoteDialog()
        bottomSheetCheckoutNoteDialog.contexts = context
        if (bottomSheetCheckoutNoteDialog.isVisible) {
            bottomSheetCheckoutNoteDialog.dismiss()
        } else {
            val args = Bundle()
            args.putString("addressTitle", addressTitle)
            args.putString("branch_id", "")
            args.putString("user_address_id", userAddressId)
            bottomSheetCheckoutNoteDialog.arguments = args
            bottomSheetCheckoutNoteDialog.show(
                (context as FragmentActivity).supportFragmentManager,
                bottomSheetCheckoutNoteDialog.tag
            )
        }
    }

}