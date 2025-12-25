package com.module.wearvocab.data

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.module.wearvocab.data.room.WordDatabase

class PhoneMessageListenerService : WearableListenerService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/word_learned") {
            val learnedWord = String(messageEvent.data)

            val db = WordDatabase.getDatabase(applicationContext)

            scope.launch {
                db.wordDao().markWordAsLearned(learnedWord)
                println("Telefonda güncellendi ve UI tetiklendi: $learnedWord")
            }
        }
    }
}
