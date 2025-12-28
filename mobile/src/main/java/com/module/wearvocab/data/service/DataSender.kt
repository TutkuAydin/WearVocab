package com.module.wearvocab.data.service

import android.content.Context
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.module.wearvocab.data.room.Word
import kotlinx.coroutines.tasks.await

class WearableManager(context: Context) {
    private val dataClient = Wearable.getDataClient(context)

    suspend fun sendWordsToWatch(words: List<Word>) {
        try {
            val request = PutDataMapRequest.create("/words_path").apply {
                val wordListStrings = words.map { "${it.englishWord}|${it.meaning}|${it.exampleSentence}|${it.audioUrl}" }
                dataMap.putStringArray("word_list", wordListStrings.toTypedArray())
                dataMap.putLong("timestamp", System.currentTimeMillis())
            }

            val putDataReq = request.asPutDataRequest()
            putDataReq.setUrgent()
            dataClient.putDataItem(putDataReq).await()
            println("Veri başarıyla saate gönderildi!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}