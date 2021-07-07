package com.foodorder.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.foodorder.repository.ProjectRepository
import com.foodorder.response.CommonResponse
import org.json.JSONObject
import utils.SessionManagement

/**
 * Created on 04-05-2020.
 */
class SplashViewModel(application: Application) : AndroidViewModel(application) {
    val context = application.applicationContext
    val projectRepository = ProjectRepository()
    val dataStoreLiveData = MutableLiveData<Boolean>()

    fun makeGetSetting(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getSettings(params)
    }

    fun makeGetCartList(params: HashMap<String, String>): LiveData<CommonResponse?> {
        return projectRepository.getCartList(params)
    }

    fun storeData(data: String) {
        val jsonObject = JSONObject(data)

        val website = jsonObject.getString("website")
        val currency = jsonObject.getString("currency")
        val currencySymbol = jsonObject.getString("currency_symbol")
        val gatewayCharges = jsonObject.getString("gateway_charges")
        val appContact = jsonObject.getString("app_contact")
        val appWhatsapp = jsonObject.getString("app_whatsapp")
        val appEmail = jsonObject.getString("app_email")
        val mobilePrefix = jsonObject.getString("mobile_prefix")
        val deliveryCharge = jsonObject.getString("delivery_charge")
        val enableCod = jsonObject.getString("enable_cod")
        val enablePayonline = jsonObject.getString("enable_payonline")

        SessionManagement.PermanentData.setSession(context, "website", website)
        SessionManagement.PermanentData.setSession(context, "currency", currency)
        SessionManagement.PermanentData.setSession(context, "currency_symbol", currencySymbol)
        SessionManagement.PermanentData.setSession(context, "gateway_charges", gatewayCharges)
        SessionManagement.PermanentData.setSession(context, "app_contact", appContact)
        SessionManagement.PermanentData.setSession(context, "app_whatsapp", appWhatsapp)
        SessionManagement.PermanentData.setSession(context, "app_email", appEmail)
        SessionManagement.PermanentData.setSession(context, "mobile_prefix", mobilePrefix)
        SessionManagement.PermanentData.setSession(context, "delivery_charge", deliveryCharge)
        SessionManagement.PermanentData.setSession(context, "enable_cod", enableCod)
        SessionManagement.PermanentData.setSession(context, "enable_payonline", enablePayonline)

        dataStoreLiveData.value = true

    }

}