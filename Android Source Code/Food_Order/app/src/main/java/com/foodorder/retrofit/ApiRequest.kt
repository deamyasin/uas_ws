package com.foodorder.retrofit

import Config.BaseURL
import com.foodorder.BuildConfig
import com.foodorder.response.CommonResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiRequest {

    @FormUrlEncoded
    @POST("index.php/rest/user/login")
    fun login(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/newuser")
    fun register(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/verifyphone")
    fun verifyPhone(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @Multipart
    @POST("index.php/rest/user/photo")
    fun updateProfileImage(
        @Part("user_id") userId: String,
        @Part image: MultipartBody.Part
    ): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/profile")
    fun updateProfile(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/settings/list")
    fun getSettings(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/update_name")
    fun updateName(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/update_phone")
    fun updatePhone(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/verify_update_phone")
    fun verifyUpdatePhone(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/resendotp")
    fun resendOtp(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/update_email")
    fun updateEmail(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/forgotpassword")
    fun forgotPassword(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/forgotpasswordverify")
    fun verifyForgotPassword(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/resetpassword")
    fun resetPassword(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/changepassword")
    fun changePassword(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/user/playerid")
    fun registerPlayerId(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/categories/list")
    fun getCategoryList(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/products/list")
    fun getProductList(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/products/details")
    fun getProductDetail(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/branches/list")
    fun getBranchList(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/address/list")
    fun getAddressList(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/address/add")
    fun addAddress(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/address/update")
    fun updateAddress(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/address/delete")
    fun deleteAddress(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/cart/list")
    fun getCartList(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/cart/add")
    fun addCartItem(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/cart/minus")
    fun removeCartItem(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/cart/delete")
    fun deleteCartItem(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/cart/clean")
    fun cleanCart(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/cart/add_option")
    fun addCartItemOption(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/cart/minus_option")
    fun removeCartItemOption(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/order/send")
    fun sendOrder(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/order/list")
    fun orderList(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/order/details")
    fun orderDetail(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/order/track")
    fun getTrackOrderList(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/cart/reorder")
    fun reOrder(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/order/cancel")
    fun cancelOrder(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/home/list")
    fun getHomeList(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/products/search")
    fun getSearchProductList(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/coupon/validate")
    fun validCoupon(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/coupon/list")
    fun getCouponList(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @FormUrlEncoded
    @POST("index.php/rest/contactus/send")
    fun sendContactUs(@FieldMap params: Map<String, String>): Call<CommonResponse>

    @GET
    fun getPaymentData(
        @Url paymentUrl: String
    ): Call<String>

}