package com.dicereligion.vagabond.feature.hangman.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicereligion.vagabond.feature.hangman.domain.HangmanGame
import com.dicereligion.vagabond.feature.hangman.domain.HangmanGameState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HangmanViewModel @Inject constructor(
    private val hangmanGame: HangmanGame
) : ViewModel() {

    private val _gameState = MutableStateFlow(HangmanGameState())
    val gameState: StateFlow<HangmanGameState> = _gameState.asStateFlow()

    fun startGame(secretWord: String? = null) {
        _gameState.value = hangmanGame.newGame(secretWord)
    }

    fun guessLetter(letter: Char) {
        _gameState.value = hangmanGame.guessLetter(_gameState.value, letter)
    }

    fun resetGame() {
        _gameState.value = HangmanGameState()
    }
}