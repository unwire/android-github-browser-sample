package com.kuba.example.projects.impl.search

import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section

fun TextView.renderMessage(message: String) {
    isVisible = true
    text = message
}

fun RecyclerView.setupAdapter(repositorySection: Section) {
    val adapter = GroupAdapter<GroupieViewHolder>()
    adapter.add(repositorySection)
    this.adapter = adapter
}
