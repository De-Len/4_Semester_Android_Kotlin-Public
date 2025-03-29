package com.example.rickandmortyapi

import androidx.lifecycle.viewModelScope
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rickandmortyapi.ViewModel.RickAndMortyCharacterViewModel
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope(testDispatcher) {

    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
        cleanupTestCoroutines()
    }
}

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RickAndMortyCharacterViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun viewModel_cancelsCoroutineOnClear() = runTest {
        val viewModel = RickAndMortyCharacterViewModel()

        viewModel.fetchCharacters()

        assert(viewModel.characters.value.isEmpty()) { "Корутина не запустилась" }

        viewModel.viewModelScope.coroutineContext.cancel()

        assertFalse("Все корутины должны быть отменены", viewModel.viewModelScope.isActive)
    }
}

