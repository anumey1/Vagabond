package com.dicereligion.vagabond.feature.hangman.domain

import androidx.compose.runtime.Immutable
import com.dicereligion.vagabond.feature.hangman.domain.ReturnMovieOrShow
import javax.inject.Inject
import javax.inject.Singleton

@Immutable // Indicates that all fields in this class are immutable.
data class HangmanGameState(
    val secretWord: String = "",
    val guessedLetters: Set<Char> = emptySet(),
    val incorrectGuesses: Int = 0,
    val maxIncorrectGuesses: Int = 7,
    val isGameOver: Boolean = false,
    val isGameWon: Boolean = false,
    val message: String = ""
) {
    val maskedWord: String
        get() = secretWord.map {
            if (it.isLetterOrDigit()) {
                if (guessedLetters.contains(it)) it else '_'
            } else if (it == ' ') {
                '/' // PRD: Spaces as '/' 
            } else {
                it // Auto-reveal other characters
            }
        }.joinToString(" ")

    val guessesLeft: Int
        get() = maxIncorrectGuesses - incorrectGuesses

    val availableLetters: List<Char>
        get() = ('A'..'Z').filter { !guessedLetters.contains(it) }
}

@Singleton
class HangmanGame @Inject constructor() {

    fun newGame(secretWord: String? = null): HangmanGameState {
        val word = if (secretWord.isNullOrBlank()) {
            selectRandomWord()
        } else {
            secretWord
        }.uppercase() // Ensure the word is always uppercase
        return HangmanGameState(secretWord = word)
    }

    fun guessLetter(currentState: HangmanGameState, letter: Char): HangmanGameState {
        if (currentState.isGameOver || currentState.guessedLetters.contains(letter)) {
            return currentState
        }

        val newGuessedLetters = currentState.guessedLetters + letter
        val (newIncorrectGuesses, message) = updateGuessCountAndMessage(currentState, letter)

        val updatedState = currentState.copy(
            guessedLetters = newGuessedLetters,
            incorrectGuesses = newIncorrectGuesses,
            message = message
        )

        return checkGameEndCondition(updatedState)
    }

    private fun selectRandomWord(): String {
        return ReturnMovieOrShow()
    }

    private fun updateGuessCountAndMessage(currentState: HangmanGameState, letter: Char):
            Pair<Int, String> {
        val isCorrectGuess = currentState.secretWord.contains(letter)

        val newIncorrectGuesses = if (isCorrectGuess) {
            currentState.incorrectGuesses
        } else {
            currentState.incorrectGuesses + 1
        }

        val message = if (isCorrectGuess) {
            "Good guess!"
        } else {
            "Wrong guess!"
        }
        return Pair(newIncorrectGuesses, message)
    }

    private fun checkGameEndCondition(currentState: HangmanGameState): HangmanGameState {
        val allLettersGuessed = currentState.secretWord.filter { it.isLetterOrDigit() }
            .all { currentState.guessedLetters.contains(it) }

        return when {
            allLettersGuessed -> currentState.copy(isGameOver = true, isGameWon = true, message = "You Win!")
            currentState.incorrectGuesses >= currentState.maxIncorrectGuesses ->
                currentState.copy(isGameOver = true, isGameWon = false, message = "You Lose!")
            else -> currentState
        }
    }
}