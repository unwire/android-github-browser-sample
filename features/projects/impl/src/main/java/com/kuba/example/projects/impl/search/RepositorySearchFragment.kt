package com.kuba.example.projects.impl.search

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kuba.example.projects.impl.R
import com.kuba.example.projects.impl.databinding.ControllerRepositorySearchBinding

class RepositorySearchFragment : Fragment(R.layout.controller_repository_search){

    private var _binding : ControllerRepositorySearchBinding? = null
    private val binding get() = _binding // This property is only valid between onCreateView and onDestroyView.

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ControllerRepositorySearchBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}