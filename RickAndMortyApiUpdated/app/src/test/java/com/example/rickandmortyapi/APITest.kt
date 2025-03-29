package com.example.rickandmortyapi

import com.example.rickandmortyapi.Model.Network.RickAndMortyApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test

import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class APITest {
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun `set up`() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun `tear down`() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetch characters successfully`() = runTest {
        val mockResponse = MockResponse()
            .setBody("{ \"results\": [{ \"id\": 1, \"name\": \"Rick Sanchez\" }] }")
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val response = RickAndMortyApi.retrofitService.getCharacters(1)
        assert(response.results.isNotEmpty()) { "Список персонажей пуст" }
        assert(response.results[0].name == "Rick Sanchez") { "Имя персонажа не совпадает" }
    }
}