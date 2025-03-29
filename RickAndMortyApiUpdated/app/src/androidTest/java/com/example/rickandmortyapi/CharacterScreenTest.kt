package com.example.rickandmortyapi

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.example.rickandmortyapi.View.CharacterScreen
import com.example.rickandmortyapi.ViewModel.RickAndMortyCharacterViewModel
import kotlinx.coroutines.test.runTest

import org.junit.Test

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CharacterScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun refreshButton_triggersCharacterFetch() = runTest {
        val viewModel = RickAndMortyCharacterViewModel()

        composeTestRule.setContent {
            CharacterScreen(viewModel = viewModel)
        }

        val initialCharacters = viewModel.characters.value

        composeTestRule.onNodeWithTag("refreshButton").performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            viewModel.characters.value != initialCharacters
        }

        assert(viewModel.characters.value.isNotEmpty()) {
            "Список персонажей не обновился после нажатия кнопки"
        }
    }
}