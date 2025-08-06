package com.example.news.presentation.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core.model.NewsItem
import com.example.core.ui.BodyTextMediumBlackDeep
import com.example.core.ui.CaptionSmallWhite
import com.example.core.ui.HeadingMediumBlueGreyCenter
import com.example.news.R
import com.example.news.utils.DateUtils

@Composable
fun NewsItemView(
    newsItem: NewsItem,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { onClick(newsItem.id) }
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .padding(top = 4.dp)
        ) {
            AsyncImage(
                model = newsItem.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .heightIn(min = 180.dp)
                    .fillMaxWidth()
            )
            Image(
                painter = painterResource(id = R.drawable.fade_view_gradient),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
        }

        Text(
            text = newsItem.title,
            style = HeadingMediumBlueGreyCenter,
            modifier = Modifier
                .padding(horizontal = 38.dp)
                .align(Alignment.CenterHorizontally)
        )

        Image(
            painter = painterResource(id = R.drawable.ic_news_decor),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 10.dp)
        )

        Text(
            text = newsItem.description,
            style = BodyTextMediumBlackDeep,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp)
                .padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = DateUtils.formatEventDates(
                    context = LocalContext.current,
                    startDateTime = newsItem.startDateTime,
                    endDateTime = newsItem.endDateTime
                ),
                style =  CaptionSmallWhite,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}

