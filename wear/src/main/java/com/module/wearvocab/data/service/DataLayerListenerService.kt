package com.module.wearvocab.data.service

import android.util.Log
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.module.wearvocab.data.room.Word
import com.module.wearvocab.data.room.WordDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class DataLayerListenerService : WearableListenerService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.dataItem.uri.path == "/words_path") {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val wordList = dataMap.getStringArray("word_list") ?: arrayOf()

                scope.launch {
                    Log.d("WEAR_DEBUG", "Veritabanı güncelleniyor...")
                    saveToLocalDatabase(wordList)
                }
            }
        }
    }

    private suspend fun saveToLocalDatabase(wordStrings: Array<String>) {
        val db = WordDatabase.Companion.getDatabase(applicationContext)
        val dao = db.wordDao()

        dao.deleteAllWords()
        val wordsToInsert = wordStrings.map {
            val parts = it.split("|")
            Word(
                englishWord = parts[0],
                meaning = parts[1],
                exampleSentence = parts.getOrElse(2) { "" },
                isLearned = false
            )
        }
        wordsToInsert.forEach { dao.insertWord(it) }

        Log.d("WEAR_DEBUG", "UI tetiklenmeli, yazılan kelime sayısı: ${wordsToInsert.size}")
    }
}