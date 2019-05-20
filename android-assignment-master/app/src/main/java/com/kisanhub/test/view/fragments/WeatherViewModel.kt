package com.kisanhub.test.view.fragments

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kisanhub.test.App
import com.kisanhub.test.BuildConfig
import com.kisanhub.test.models.Rainfall
import com.kisanhub.test.service.WeatherService
import com.kisanhub.test.util.Urls
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class WeatherViewModel : ViewModel() {

    private val weatherDataList = MutableLiveData<List<Rainfall>>()

    var isLoading = ObservableField<Boolean>()

    private fun setWeatherDataList(weatherDataList: List<Rainfall>) {
        isLoading.set(false)
        this.weatherDataList.postValue(weatherDataList)
    }

    fun getWeatherData(location: String, metric: String): MutableLiveData<List<Rainfall>> ?{

        val baseUrl = BuildConfig.BASE_URL + Urls.GET_WATHER_URL

        val okHttpClientBuilder = OkHttpClient.Builder()
        val retrofitBuilder = Retrofit.Builder()
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
        val retrofitService = retrofitBuilder.build().create(WeatherService::class.java)

        isLoading.set(true)

        retrofitService.getData(metric, location).enqueue(object : Callback<List<Rainfall>> {

            override fun onResponse(call: Call<List<Rainfall>>, response: Response<List<Rainfall>>) {

                response.body()?.let { rainfallList ->
                    App.applicationContext().saveWeatherData(location + "" + metric, rainfallList.filter { it.year == 1910 })
                    setWeatherDataList(rainfallList.filter { it.year == 1910 })
                }
            }

            override fun onFailure(call: Call<List<Rainfall>>, t: Throwable) {
                setWeatherDataList(Collections.emptyList<Rainfall>())
            }
        })

        return weatherDataList;
    }
}