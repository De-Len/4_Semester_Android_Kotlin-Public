package com.example.weatherapp.View

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.weatherapp.Model.Adapter
import com.example.weatherapp.Model.DiffCallback
import com.example.weatherapp.FavoritesActivity
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: Adapter
    private val FAVORITES_REQUEST_CODE = 1
    private val viewModel: MainViewModel by viewModels()
    private var cityName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Log.d("AAA", "Activity created")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val rView: RecyclerView = findViewById(R.id.rView)
        adapter = Adapter(DiffCallback())
        rView.adapter = adapter
        rView.layoutManager = LinearLayoutManager(this)
        val etSearch: EditText = findViewById(R.id.et_search)
        val btnSearch: Button = findViewById(R.id.btn_search)
        val changeTemperatureButton: ImageView = findViewById(R.id.change_temperature)
        val cityNameTextView: TextView = findViewById(R.id.cityName)

        var TemperatureButtonImage = R.drawable.celsius
        changeTemperatureButton.setBackgroundResource(TemperatureButtonImage)

        // подписка
        viewModel.getResultLiveDataWeather().observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.getResultLiveDataCityName().observe(this, Observer {
            cityNameTextView.text = "Название города: " + it
        })


        btnSearch.setOnClickListener {
            val cityNameInSearch = etSearch.text.toString().trim()

            viewModel.search(cityNameInSearch) {}
        }

        changeTemperatureButton.setOnClickListener {
            if (TemperatureButtonImage == R.drawable.celsius) {
                adapter.updateData("%.0f °F", .0)
                TemperatureButtonImage = R.drawable.fahrenheit
            }
            else {
                adapter.updateData("%.0f °C", 273.15)
                TemperatureButtonImage = R.drawable.celsius
            }
            changeTemperatureButton.setBackgroundResource(TemperatureButtonImage)
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == FAVORITES_REQUEST_CODE && resultCode == RESULT_OK) {
//            data?.getStringExtra("cityName")?.let { cityName ->
//                val etSearch: EditText = findViewById(R.id.et_search)
//                etSearch.setText(cityName)
//                fetchWeather(cityName)
//            }
//        }
//    }

}