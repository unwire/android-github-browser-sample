package com.kuba.example.users.impl.userdetails

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kuba.example.service.api.UserDetails
import com.kuba.example.users.impl.R
import com.kuba.example.users.impl.databinding.FragmentUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {

    private val viewModel: UserDetailsViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.coroutineScope.launch {
            viewModel.loadUserDetails()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentUserDetailsBinding.bind(view)
        val composeView = binding.composeView

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collectLatest { uiModel ->
                    composeView.apply {
                        // Dispose of the Composition when the view's LifecycleOwner is destroyed
                        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                        setContent {
                            MaterialTheme {
                                when (uiModel) {
                                    UserDetailsUiModel.Loading -> CircularProgressIndicator()
                                    is UserDetailsUiModel.Error -> Text(uiModel.message)
                                    is UserDetailsUiModel.Content -> UserDetailsView(
                                        user = uiModel.userDetails,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun UserDetailsView(
    user: UserDetails,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            val context = LocalContext.current
            val imageRequest = ImageRequest.Builder(context)
                .data(user.avatarUrl)
                .crossfade(true)
                // TODO: Add a proper placeholder
                .placeholder(androidx.core.R.drawable.notification_bg)
                .build()
            AsyncImage(
                model = imageRequest,
                // Decorative image - does not need content description
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .blur(100.dp)
            )
            AsyncImage(
                model = imageRequest,
                // Decorative image - does not need content description
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .align(Alignment.Center)
            )
        }
        Column(
            Modifier
                .weight(1f)
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            user.name?.let { userName ->
                Text(
                    text = userName,
                    style = MaterialTheme.typography.h3,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.login,
                color = Color.Gray,
                style = MaterialTheme.typography.h4
            )
            Spacer(modifier = Modifier.height(32.dp))
            InfoWithIconRow(Icons.Outlined.Face) {
                Text(
                    text = buildAnnotatedString {
                        append(user.followers.toString())
                        withStyle(style = SpanStyle(color = Color.Gray)) {
                            append(" followers")
                        }
                        append(" | ")
                        append(user.following.toString())
                        withStyle(style = SpanStyle(color = Color.Gray)) {
                            append(" following")
                        }
                    }
                )
            }
            user.name?.let { userLocation ->
                InfoWithIconRow(Icons.Outlined.LocationOn) {
                    Text(userLocation)
                }
            }
            user.email?.let { userEmail ->
                InfoWithIconRow(Icons.Outlined.Email) {
                    Text(userEmail)
                }
            }
        }
    }
}

@Composable
private fun InfoWithIconRow(
    imageVector: ImageVector,
    content: @Composable () -> Unit
) {
    Row(Modifier.padding(8.dp)) {
        Icon(
            imageVector = imageVector,
            // Decorative image - does not need content description
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(8.dp))
        content()
    }
}

@Preview
@Composable
fun UserDetailsViewPreview() {
    val userDetails = UserDetails(
        login = "dr-dre",
        name = "Dr. Dre",
        email = "dre@gmail.com",
        followers = 1532,
        following = 1,
        location = "West Coast",
        avatarUrl = null
    )

    MaterialTheme {
        Surface {
            UserDetailsView(user = userDetails, modifier = Modifier.fillMaxSize())
        }
    }
}
