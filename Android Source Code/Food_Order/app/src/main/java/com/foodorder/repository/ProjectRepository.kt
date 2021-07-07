package com.foodorder.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.foodorder.response.CommonResponse
import com.foodorder.retrofit.ApiRequest
import com.foodorder.retrofit.RetrofitRequest.retrofitInstance
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created on 04-05-2020.
 */
class ProjectRepository {
    private val apiRequest: ApiRequest = retrofitInstance!!.create(
        ApiRequest::class.java
    )
    val gsonBuilder = GsonBuilder().create()
    val gson = Gson()

    companion object {
        private val TAG = ProjectRepository::class.java.simpleName
    }

    fun checkLogin(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.login(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun register(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.register(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getSettings(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getSettings(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun verifyPhone(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.verifyPhone(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun resendOtp(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.resendOtp(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun changePassword(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.changePassword(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun resetPassword(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.resetPassword(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun forgotPassword(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.forgotPassword(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun verifyForgotPassword(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.verifyForgotPassword(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun verifyUpdatePhone(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.verifyUpdatePhone(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun updateName(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.updateName(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun updateEmail(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.updateEmail(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun updatePhone(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.updatePhone(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun uploadProfilePicture(
        userId: String,
        image: MultipartBody.Part
    ): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$userId")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.updateProfileImage(userId, image).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun registerPlayerId(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.registerPlayerId(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getCategoryList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getCategoryList(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getProductList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getProductList(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getProductDetail(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getProductDetail(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getBranchList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getBranchList(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getAddressList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getAddressList(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun addAddress(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.addAddress(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun updateAddress(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.updateAddress(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun deleteAddress(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.deleteAddress(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getCartList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getCartList(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun addCartItem(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.addCartItem(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun removeCartItem(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.removeCartItem(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun deleteCartItem(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.deleteCartItem(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun cleanCart(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.cleanCart(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun addCartItemOption(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.addCartItemOption(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun removeCartItemOption(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.removeCartItemOption(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun sendOrder(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.sendOrder(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun orderList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.orderList(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun orderDetail(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.orderDetail(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getTrackOrderList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getTrackOrderList(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun reOrder(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.reOrder(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun cancelOrder(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.cancelOrder(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getHomeList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getHomeList(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getSearchProductList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getSearchProductList(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getCouponList(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.getCouponList(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun validCoupon(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.validCoupon(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun sendContactUs(params: Map<String, String>): LiveData<CommonResponse?> {
        Log.d(TAG, "Post Data::$params")
        val data = MutableLiveData<CommonResponse?>()
        apiRequest.sendContactUs(params).enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                if (response.body() != null) {
                    Log.d(TAG, "onResponse data:: ${gson.toJson(response.body())}")
                    val commonResponse = response.body()!!
                    commonResponse.data = gson.toJson(commonResponse.data)
                    data.value = commonResponse
                }
            }

            override fun onFailure(
                call: Call<CommonResponse>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

    fun getPaymentData(paymentUrl: String): LiveData<String?> {
        val data = MutableLiveData<String?>()
        Log.d(TAG, "URL::$paymentUrl")
        apiRequest.getPaymentData(paymentUrl).enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "onResponse response:: $response")
                Log.d(TAG, "onResponse response data:: ${response.body()}")
                data.value = response.body()
            }

            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.d(TAG, "onFailure error:: ${t.printStackTrace()}")
                data.value = null
            }
        })
        return data
    }

}