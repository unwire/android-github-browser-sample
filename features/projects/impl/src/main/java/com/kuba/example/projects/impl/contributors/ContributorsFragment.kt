package com.kuba.example.projects.impl.contributors

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kuba.example.projects.api.navigation.ContributorsScreen
import com.kuba.example.projects.impl.R
import com.kuba.example.projects.impl.databinding.ControllerContributorsBinding

class ContributorsFragment : Fragment(R.layout.controller_contributors) {
    private var _binding : ControllerContributorsBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ControllerContributorsBinding.bind(view)
        val args = requireArguments()
        val contributorScreenArgs = ContributorsScreen.extractArgs(args)
        with(binding) {
            with(contributorScreenArgs) {
                lblName.text = repoName
                lblDescription.text = repoDescription
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}