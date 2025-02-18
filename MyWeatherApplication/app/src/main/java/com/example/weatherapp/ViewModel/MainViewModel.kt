package com.example.weatherapp.ViewModel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.ContextWrapper
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.Model.Network.Common
import com.example.weatherapp.Model.Network.RetrofitServices
import com.example.weatherapp.Model.WeatherEntry
import com.example.weatherapp.Model.WeatherForecast
import com.example.weatherapp.R
import com.example.weatherapp.View.MainActivity
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


class MainViewModel : ViewModel() {
    private var mService: RetrofitServices = Common.retrofitService
    var isSityExist: Boolean = true
    private var liveDataWeather = MutableLiveData<List<WeatherEntry>>()
    fun getResultLiveDataWeather(): LiveData<List<WeatherEntry>> {
        return liveDataWeather
    }

    private var liveDataCityName = MutableLiveData<String>()
    fun getResultLiveDataCityName(): MutableLiveData<String> {
        return liveDataCityName
    }

    private fun fetchWeather(cityName: String, callback: (List<WeatherEntry>?) -> Unit) {
        val appid = BuildConfig.OPEN_WEATHER_API_KEY
        mService.getWeatherList(cityName, appid).enqueue(object : Callback<WeatherForecast> {
            override fun onResponse(call: Call<WeatherForecast>, response: Response<WeatherForecast>) {
                if (response.isSuccessful) {
                    val forecastList = response.body()?.list
                    liveDataCityName.value = cityName
                    if (forecastList != null) {
                        callback(forecastList) // Возвращаем данные через callback
                    } else {
                        callback(null) // Если данные отсутствуют
                    }
                } else {
                    Log.e("fetchWeather", "Ошибка при запросе погоды: ${response.message()}")
                    liveDataCityName.value = "Город не существует"
                    callback(null) // Если ответ не успешный
                }
            }

            override fun onFailure(call: Call<WeatherForecast>, t: Throwable) {
                // Логируем ошибку
                Log.e("fetchWeather", "Ошибка при запросе погоды: ${t.message}")
                callback(null) // Возвращаем null в случае ошибки
            }
        })
    }

    init {
        Log.d("AAA", "VM created")
    }

    override fun onCleared() {
        Log.d("AAA", "VM cleared")
        super.onCleared()
    }

    fun search(cityNameInSearch: String, callback: (List<WeatherEntry>?) -> Unit) {
        if (cityNameInSearch.isNotEmpty()) {
            fetchWeather(cityNameInSearch) { liveDataWeather.value = it }
        } else {
            // Если cityNameInSearch пустая, возвращаем null
            liveDataWeather.value = null
            callback(null)
        }
    }
}