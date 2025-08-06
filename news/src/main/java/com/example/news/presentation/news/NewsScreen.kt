package com.example.news.presentation.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.core.ui.BodyTextRegularBlackDeep
import com.example.news.R

@Composable
fun NewsScreen(
    state: NewsState,
    onEvent: (NewsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            NewsTopAppBar(
                onFilterClick = { onEvent(NewsEvent.FiltersClicked) }
            )
        }
    )
    { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (state) {
                is NewsState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                is NewsState.NoResults -> {
                    Text(
                        text = stringResource(R.string.no_news_available),
                        style = BodyTextRegularBlackDeep,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }

                is NewsState.Results -> {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            bottom = 60.dp,
                            start = 8.dp,
                            end = 8.dp
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.newsList) { newsItem ->
                            NewsItemView(
                                newsItem = newsItem,
                                onClick = { clickedNewsId: Int ->
                                    onEvent(NewsEvent.NewsClicked(newsId = clickedNewsId))
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                is NewsState.Error -> {
                    Text(
                        text = stringResource(R.string.no_news_available),
                        style = BodyTextRegularBlackDeep,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

