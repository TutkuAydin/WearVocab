package com.module.wearvocab.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.module.wearvocab.R
import com.module.wearvocab.presentation.viewmodel.WordIntent
import com.module.wearvocab.presentation.viewmodel.WordViewModel
import com.module.wearvocab.presentation.components.AddWordSection
import com.module.wearvocab.presentation.components.WordList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen(viewModel: WordViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.tab_to_learn),
        stringResource(R.string.tab_learned)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                CenterAlignedTopAppBar(title = { Text(stringResource(R.string.app_title)) })
                PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        },
        containerColor = Color.Gray
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (selectedTabIndex == 0) {
                AddWordSection(
                    onSearch = { englishWord->
                        viewModel.handleIntent(WordIntent.FetchAndSaveWord(englishWord, context))
                    }
                )
            }

            val filteredWords = remember(state.words, selectedTabIndex) {
                if (selectedTabIndex == 0) {
                    state.words.filter { !it.isLearned }
                } else {
                    state.words.filter { it.isLearned }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (filteredWords.isEmpty()) {
                    EmptyStateMessage(selectedTabIndex)
                } else {
                    WordList(
                        words = filteredWords,
                        onSwipe = { word ->
                            viewModel.handleIntent(WordIntent.ToggleLearned(word))
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun EmptyStateMessage(tabIndex: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (tabIndex == 0) stringResource(R.string.empty_to_learn) else stringResource(R.string.empty_learned),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
