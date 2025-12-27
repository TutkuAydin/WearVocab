package com.module.wearvocab.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import com.module.wearvocab.R
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.*
import com.module.wearvocab.presentation.components.WordCard
import com.module.wearvocab.presentation.components.WordDetailScreen

@Composable
fun WearWordScreen(viewModel: WearWordViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val listState = rememberScalingLazyListState()
    val context = LocalContext.current

    if (state.selectedWord != null) {
        WordDetailScreen(
            word = state.selectedWord!!,
            onMarkLearned = {
                viewModel.handleIntent(WearWordIntent.MarkAsLearned(state.selectedWord!!, context))
                viewModel.handleIntent(WearWordIntent.SelectWord(null))
            },
            onBack = {
                viewModel.handleIntent(WearWordIntent.SelectWord(null))
            }
        )
    } else {
        Scaffold(
            timeText = { TimeText() },
            positionIndicator = { PositionIndicator(scalingLazyListState = listState) }
        ) {
            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.words.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.wear_all_done),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.body2
                    )
                }
            } else {
                ScalingLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    autoCentering = AutoCenteringParams(itemIndex = 0)
                ) {
                    items(state.words) { word ->
                        WordCard(
                            word = word,
                            onClick = {
                                viewModel.handleIntent(WearWordIntent.SelectWord(word))
                            },
                            onMarkLearned = {
                                viewModel.handleIntent(WearWordIntent.MarkAsLearned(word, context))
                            }
                        )
                    }
                }
            }
        }
    }
}
