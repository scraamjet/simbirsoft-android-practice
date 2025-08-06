package com.example.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.auth.R
import com.example.core.ui.HeadingMediumWhite
import com.example.core.R.drawable

@Composable
fun AuthorizationTopAppBar(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.title_authorization),
            style = HeadingMediumWhite
        )
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = drawable.ic_back),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

