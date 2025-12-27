package com.module.wearvocab.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.module.wearvocab.data.api.RetrofitClient
import com.module.wearvocab.data.service.WearableManager
import com.module.wearvocab.data.room.Word
import com.module.wearvocab.data.room.WordDao
import com.module.wearvocab.presentation.extension.showToast
import com.module.wearvocab.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordViewModel(
    private val dao: WordDao,
    private val wearableManager: WearableManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(WordUiState(isLoading = true))
    val uiState: StateFlow<WordUiState> = _uiState.asStateFlow()

    init {
        handleIntent(WordIntent.LoadWords)
    }

    private fun observeWords() {
        viewModelScope.launch {
            dao.getAllWords()
                .flowOn(Dispatchers.IO)
                .collect { newList ->
                    _uiState.update { it.copy(words = newList, isLoading = false) }
                    wearableManager.sendWordsToWatch(newList.filter { !it.isLearned })
                }
        }
    }

    fun handleIntent(intent: WordIntent) {
        when (intent) {
            is WordIntent.FetchAndSaveWord -> fetchAndSaveWord(intent.englishWord, intent.context)
            is WordIntent.ToggleLearned -> updateWord(intent.word)
            WordIntent.LoadWords -> observeWords()
        }
    }

    private fun updateWord(word: Word) {
        val updatedList = _uiState.value.words.map {
            if (it.id == word.id) it.copy(isLearned = !word.isLearned) else it
        }
        _uiState.update { it.copy(words = updatedList) }

        viewModelScope.launch(Dispatchers.IO) {
            dao.updateWord(word.copy(isLearned = !word.isLearned))
        }
    }

    private fun fetchAndSaveWord(englishWord: String, context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = RetrofitClient.api.getDefinition(englishWord)
                if (response.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        context.showToast(R.string.error_word_not_found)
                    }
                    return@launch
                }

                val firstEntry = response[0]
                val meaning =
                    firstEntry.meanings.firstOrNull()?.definitions?.firstOrNull()?.definition
                val example =
                    firstEntry.meanings.firstOrNull()?.definitions?.firstOrNull()?.example.orEmpty()

                if (meaning != null) {
                    val newWord = Word(
                        englishWord = englishWord.replaceFirstChar { it.uppercase() },
                        meaning = meaning,
                        exampleSentence = example,
                        isLearned = false
                    )
                    dao.insertWord(
                        newWord
                    )

                    withContext(Dispatchers.Main) {
                        context.showToast(R.string.success_word_added)
                    }

                } else {
                    withContext(Dispatchers.Main) {
                        context.showToast(R.string.error_meaning_not_found)
                    }
                }

            } catch (_: Exception) {
                withContext(Dispatchers.Main) {
                    context.showToast(R.string.error_network_or_api)
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
