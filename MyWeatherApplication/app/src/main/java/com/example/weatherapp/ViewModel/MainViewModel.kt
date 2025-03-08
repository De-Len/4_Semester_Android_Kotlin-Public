package com.example.weatherapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.Model.Network.Common
import com.example.weatherapp.Model.Network.RetrofitServices
import com.example.weatherapp.Model.WeatherEntry
import com.example.weatherapp.Model.WeatherForecast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val mService: RetrofitServices = Common.retrofitService

    // LiveData для хранения данных о погоде
    private val _liveDataWeather = MutableLiveData<List<WeatherEntry>>()
    val liveDataWeather: LiveData<List<WeatherEntry>> get() = _liveDataWeather

    // LiveData для хранения названия города
    private val _liveDataCityName = MutableLiveData<String>()
    val liveDataCityName: LiveData<String> get() = _liveDataCityName

    init {
        Log.d("AAA", "VM created")
    }

    override fun onCleared() {
        Log.d("AAA", "VM cleared")
        super.onCleared()
    }

    // Метод для поиска погоды по названию города
    fun search(cityName: String) {
        if (cityName.isNotEmpty()) {
            fetchWeather(cityName)
        } else {
            // Если cityName пустой, очищаем данные
            _liveDataWeather.value = null
            _liveDataCityName.value = "Введите название города"
        }
    }

    // Приватный метод для выполнения запроса погоды
    private fun fetchWeather(cityName: String) {
        val appid = BuildConfig.OPEN_WEATHER_API_KEY
        mService.getWeatherList(cityName, appid).enqueue(object : Callback<WeatherForecast> {
            override fun onResponse(call: Call<WeatherForecast>, response: Response<WeatherForecast>) {
                if (response.isSuccessful) {
                    val forecastList = response.body()?.list
                    _liveDataCityName.value = cityName
                    _liveDataWeather.value = forecastList ?: emptyList()
                } else {
                    Log.e("fetchWeather", "Ошибка при запросе погоды: ${response.message()}")
                    _liveDataCityName.value = "Город не существует"
                    _liveDataWeather.value = emptyList()
                }
            }

            override fun onFailure(call: Call<WeatherForecast>, t: Throwable) {
                Log.e("fetchWeather", "Ошибка при запросе погоды: ${t.message}")
                _liveDataCityName.value = "Ошибка сети"
                _liveDataWeather.value = emptyList()
            }
        })
    }
}