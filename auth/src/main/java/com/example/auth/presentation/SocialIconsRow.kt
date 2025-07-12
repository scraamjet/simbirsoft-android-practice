package com.example.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.auth.R

@Composable
fun SocialIconsRow() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_social_vk),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(horizontal = 8.dp),
            tint = Color.Unspecified
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_social_fb),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(horizontal = 8.dp),
            tint = Color.Unspecified
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_social_ok),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .padding(horizontal = 8.dp),
            tint = Color.Unspecified
        )
    }
}
