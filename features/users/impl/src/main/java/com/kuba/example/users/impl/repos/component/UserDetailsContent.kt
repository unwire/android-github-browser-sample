package com.kuba.example.users.impl.repos.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuba.example.service.api.User
import com.kuba.example.users.impl.R

@Composable
fun UserDetailsContent(
    user: User,
    imageContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),

        ) {
        imageContent()
        DetailsTextItem(
            text = user.login,
            style = MaterialTheme.typography.bodySmall
        )
        DetailsTextItem(
            text = user.name ?: "No name",
            style = MaterialTheme.typography.bodyLarge
        )
        FollowingHandle(
            followerCount = user.followers,
            followingCount = user.following
        )
        DetailsTextItem(text = user.bio ?: "No bio available")
    }
}

@Composable
private fun DetailsTextItem(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
) {
    Text(
        modifier = modifier.padding(8.dp),
        text = text,
        textAlign = TextAlign.Center,
        style = style,
    )
}

@Composable
private fun FollowingHandle(
    modifier: Modifier = Modifier,
    followerCount: Int = 0,
    followersLabel: String = stringResource(id = R.string.followers),
    followingCount: Int = 0,
    followingLabel: String = stringResource(R.string.following),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Person,
            contentDescription = stringResource(id = R.string.followers),
        )
        Spacer(modifier = Modifier.size(8.dp))
        DetailsTextItem(
            text = followerCount.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
        DetailsTextItem(text = followersLabel)
        DetailsTextItem(text = " - ")
        DetailsTextItem(
            text = followingCount.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
        DetailsTextItem(text = followingLabel)
    }
}

@Preview(showBackground = true)
@Composable
private fun UserDetailsContentPreview() {
    MaterialTheme {
        UserDetailsContent(
            modifier = Modifier.padding(8.dp),
            user = User(
                login = "login",
                name = "name",
                avatarUrl = "avatarUrl"
            ),
            imageContent = {
                Image(
                    painter = painterResource(id = R.drawable.person_24),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            })
    }
}

@Preview(showBackground = true)
@Composable
private fun FollowingHandlePreview() {
    MaterialTheme {
        FollowingHandle()
    }
}