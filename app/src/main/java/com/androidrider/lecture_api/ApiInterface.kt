package com.androidrider.lecture_api

import retrofit2.Call
import retrofit2.http.GET

// Step-3

interface ApiInterface {

    @GET("products")
    fun getProductData() : Call<MyData>
}