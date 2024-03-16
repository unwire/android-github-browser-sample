package com.kuba.example.users.impl.repos

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.lifecycleScope
import com.kuba.example.service.api.User
import com.kuba.example.users.impl.R
import com.kuba.example.users.impl.databinding.FragmentUserDetailsBinding
import com.kuba.example.users.impl.repos.component.ErrorOrEmpty
import com.kuba.example.users.impl.repos.component.Loading
import com.kuba.example.users.impl.repos.component.NetworkImageContent
import com.kuba.example.users.impl.repos.component.UserDetailsContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsFragment : Fragment(R.layout.fragment_user_details) {
    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val viewModel: UserDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUserDetailsBinding.bind(view)

        lifecycleScope.launch {
            viewModel.state.collect { uiState ->
                setContent(uiState)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycleScope.launch {
            viewModel.loadUserDetails()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setContent(uiState: UserDetailsUiModel) {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    UserDetailsView(uiState) { user ->
                        UserDetailsContent(
                            user = user,
                            imageContent = {
                                user.avatarUrl?.let { avatarUrl ->
                                    NetworkImageContent(
                                        avatarUrl = avatarUrl,
                                    )
                                } ?: Image(
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
                }
            }
        }
    }

    @Composable
    private fun UserDetailsView(
        uiModel: UserDetailsUiModel,
        loadingComposable: @Composable () -> Unit = { Loading() },
        errorComposable: @Composable (String) -> Unit = { ErrorOrEmpty(message = it) },
        successComposable: @Composable (User) -> Unit,
    ) {
        when (uiModel) {
            is UserDetailsUiModel.Loading -> loadingComposable()
            is UserDetailsUiModel.Error -> errorComposable(uiModel.message)
            is UserDetailsUiModel.Content -> successComposable(uiModel.user)
            is UserDetailsUiModel.Idle -> { // no-op
            }
        }
    }
}
