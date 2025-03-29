package com.example.rickandmortyapi.Model.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RickAndMortyApi {
    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api/"

        val retrofitService: RickAndMortyApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RickAndMortyApiService::class.java)
        }
    }
}