/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.module.wearvocab.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.room.Room
import androidx.wear.compose.material.MaterialTheme
import com.module.wearvocab.data.room.WordDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            WordDatabase::class.java, "word-database-wear"
        ).fallbackToDestructiveMigration().build()

        val dao = db.wordDao()

        val viewModel = WearWordViewModel(dao)

        setContent {
            // Wear OS Material Theme
            MaterialTheme {
                WearWordScreen(viewModel)
            }
        }
    }
}
