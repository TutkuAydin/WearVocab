package com.module.wearvocab.data

import android.util.Log
import androidx.room.Room
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import com.module.wearvocab.data.room.Word
import com.module.wearvocab.data.room.WordDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DataLayerListenerService : WearableListenerService() {


    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.dataItem.uri.path == "/words_path") {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val wordList = dataMap.getStringArray("word_list") ?: arrayOf()

                runBlocking {
                    Log.d("WEAR_DEBUG", "Veritabanı işlemi başlıyor...")
                    saveToLocalDatabase(wordList)
                    Log.d("WEAR_DEBUG", "Veritabanı işlemi bitti!")
                }
            }
        }
    }

    private suspend fun saveToLocalDatabase(wordStrings: Array<String>) {
        withContext(Dispatchers.IO) {
            val db = Room.databaseBuilder(
                applicationContext,
                WordDatabase::class.java, "word-database-wear"
            ).build()

            val dao = db.wordDao()

            dao.deleteAllWords()

            val wordsToInsert = wordStrings.map {
                val parts = it.split("|")
                Word(
                    englishWord = parts[0],
                    meaning = parts[1],
                    exampleSentence = parts.getOrElse(2) { "" }, // Güvenlik için
                    isLearned = false
                )
            }

            wordsToInsert.forEach { dao.insertWord(it) }
            db.close()
        }
    }
}
