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
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.weatherapp.Model.Adapter
import com.example.weatherapp.Model.DiffCallback
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: Adapter
    private val viewModel: MainViewModel by viewModels()
    private var temperatureButtonImage = R.drawable.celsius

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Log.d("AAA", "Activity created")

        setupWindowInsets()
        setupToolbar()
        setupRecyclerView()
        setupViews()
        setupObservers()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setupRecyclerView() {
        val rView: RecyclerView = findViewById(R.id.rView)
        adapter = Adapter(DiffCallback())
        rView.adapter = adapter
        rView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupViews() {
        val etSearch: EditText = findViewById(R.id.et_search)
        val btnSearch: Button = findViewById(R.id.btn_search)
        val changeTemperatureButton: ImageView = findViewById(R.id.change_temperature)

        changeTemperatureButton.setBackgroundResource(temperatureButtonImage)

        btnSearch.setOnClickListener {
            val cityNameInSearch = etSearch.text.toString().trim()
            viewModel.search(cityNameInSearch)
        }

        changeTemperatureButton.setOnClickListener {
            toggleTemperatureUnit(changeTemperatureButton)
        }
    }

    private fun setupObservers() {
        viewModel.liveDataWeather.observe(this, Observer { weatherEntries ->
            adapter.submitList(weatherEntries)
        })

        viewModel.liveDataCityName.observe(this, Observer { cityName ->
            findViewById<TextView>(R.id.cityName).text = "Название города: $cityName"
        })
    }

    private fun toggleTemperatureUnit(button: ImageView) {
        if (temperatureButtonImage == R.drawable.celsius) {
            adapter.updateData("%.0f °F", .0)
            temperatureButtonImage = R.drawable.fahrenheit
        } else {
            adapter.updateData("%.0f °C", 273.15)
            temperatureButtonImage = R.drawable.celsius
        }
        button.setBackgroundResource(temperatureButtonImage)
    }
}