package com.module.wearvocab.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.OutlinedChip
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.module.wearvocab.R
import com.module.wearvocab.data.room.Word

@Composable
fun WordDetailScreen(
    word: Word,
    onMarkLearned: () -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScalingLazyListState()

    Scaffold(
        timeText = { TimeText() },
        positionIndicator = { PositionIndicator(scalingLazyListState = scrollState) }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
            autoCentering = AutoCenteringParams(itemIndex = 0)
        ) {
            item {
                Text(
                    text = word.englishWord,
                    style = MaterialTheme.typography.display3,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                Card(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.label_meaning),
                            style = MaterialTheme.typography.caption1,
                            color = MaterialTheme.colors.secondary
                        )
                        Text(
                            text = word.meaning,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }

            if (word.exampleSentence.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.label_example),
                        style = MaterialTheme.typography.caption2,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                    )
                }
                item {
                    Text(
                        text = "\"${word.exampleSentence}\"",
                        style = MaterialTheme.typography.body2,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Chip(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onMarkLearned,
                    label = { Text(text = stringResource(R.string.btn_mark_learned)) },
                    icon = { Icon(Icons.Default.Check, contentDescription = null) },
                    colors = ChipDefaults.primaryChipColors()
                )
            }

            item {
                OutlinedChip(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(R.string.btn_back)) },
                    onClick = onBack
                )
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun WordDetailPreview() {
    val testWord = Word(
        id = 1,
        englishWord = "Persistent",
        meaning = "Israrcı",
        exampleSentence = "She is very persistent in her work."
    )

    MaterialTheme {
        WordDetailScreen(
            word = testWord,
            onMarkLearned = {},
            onBack = {}
        )
    }
}
