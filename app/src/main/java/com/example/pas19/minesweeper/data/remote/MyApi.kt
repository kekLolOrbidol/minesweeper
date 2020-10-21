package com.example.pas19.minesweeper.data.remote

import retrofit2.Call
import retrofit2.http.GET

interface MyApi {
    @GET(".")
    fun getModel() : Call<ApiModel>
}