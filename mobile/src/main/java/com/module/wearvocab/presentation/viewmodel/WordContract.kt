package com.module.wearvocab.presentation.viewmodel

import com.module.wearvocab.data.room.Word

data class WordUiState(
    val words: List<Word> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class WordIntent {
    data class AddWord(val eng: String, val tr: String, val sentence: String) : WordIntent()
    data class ToggleLearned(val word: Word) : WordIntent()
    object LoadWords : WordIntent()
}