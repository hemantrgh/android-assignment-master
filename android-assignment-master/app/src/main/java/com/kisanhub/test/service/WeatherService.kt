package com.kisanhub.test.service

import com.kisanhub.test.models.Rainfall
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("{metric}-{location}.json")
    fun getData(@Path("metric") metric: String, @Path("location") location: String): Call<List<Rainfall>>
}