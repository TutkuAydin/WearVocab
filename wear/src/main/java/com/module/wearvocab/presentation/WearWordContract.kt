package com.module.wearvocab.presentation

import com.module.wearvocab.data.room.Word

data class WearWordUiState(
    val words: List<Word> = emptyList(),
    val isLoading: Boolean = false,
    val selectedWord: Word? = null
)

sealed class WearWordIntent {
    data class MarkAsLearned(val word: Word, val context: android.content.Context) :
        WearWordIntent()

    object LoadWords : WearWordIntent()

    data class SelectWord(val word: Word?) : WearWordIntent()
}
