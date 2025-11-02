package com.dicereligion.vagabond.feature.hangman.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

import com.dicereligion.vagabond.feature.hangman.R

@Singleton
class HangmanRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getWordList(): List<String> {
        return context.resources.getStringArray(R.array.hangman_word_list).toList()
    }
}