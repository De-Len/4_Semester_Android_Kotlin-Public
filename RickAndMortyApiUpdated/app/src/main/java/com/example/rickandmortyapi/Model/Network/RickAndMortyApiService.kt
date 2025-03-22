package com.example.rickandmortyapi.Model.Network

import com.example.rickandmortyapi.Model.RickAndMortyCharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") pageNumber: Int
    ): RickAndMortyCharacterResponse
}