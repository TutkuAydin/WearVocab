package com.module.wearvocab.presentation.viewmodel

import android.content.Context
import com.module.wearvocab.data.room.Word

data class WordUiState(
    val words: List<Word> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class WordIntent {
    data class FetchAndSaveWord(val englishWord: String, val context: Context) : WordIntent()
    data class ToggleLearned(val word: Word) : WordIntent()
    object LoadWords : WordIntent()
}
