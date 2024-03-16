package com.kuba.example.users.impl.repos.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun NetworkImageContent(
    avatarUrl: String,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp
) {
    AsyncImage(
        model = avatarUrl,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier
            .clip(shape = CircleShape)
            .size(size)
    )
}