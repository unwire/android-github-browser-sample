package com.kuba.example.users.impl.repos.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuba.example.users.impl.R

@Composable
fun UserDetailsImage(
    modifier: Modifier = Modifier,
    imageContent: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Box( modifier = modifier
            .size(200.dp)
            .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) { imageContent() }
    }
}

@Preview
@Composable
private fun UserDetailsImagePreview() {
    UserDetailsImage(
        imageContent = {
            Image(
                painter = painterResource(id = R.drawable.person_24),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    )
}