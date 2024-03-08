package com.kuba.example.navigation.api

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.LifecycleController

abstract class BaseController(
  @LayoutRes private val layoutRes: Int,
  args: Bundle? = null
) : LifecycleController(args) {

  init {
    addLifecycleListener(object : LifecycleListener() {
      override fun postCreateView(controller: Controller, view: View) {
        onViewCreated(view)
      }
    })
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup,
    savedViewState: Bundle?
  ): View {
    return inflater.inflate(layoutRes, container, false)
  }

  open fun onViewCreated(view: View) = Unit
}
