package com.kuba.example.projects.impl.contributors

import android.view.View
import coil.load
import coil.transform.CircleCropTransformation
import com.kuba.example.projects.impl.R
import com.kuba.example.projects.impl.databinding.ItemContributorBinding
import com.kuba.example.service.api.User
import com.xwray.groupie.viewbinding.BindableItem
import timber.log.Timber

data class UserItem(
    val user: User,
) : BindableItem<ItemContributorBinding>() {

    var onClickListener: ((user: User) -> Unit)? = null

    override fun bind(binding: ItemContributorBinding, p1: Int) {
        with(binding) {
            lblName.text = user.name ?: user.login

            user.avatarUrl?.let { url ->
                imgAvatar.load(url) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
            }

            root.setOnClickListener {
                Timber.d("Clicking contributor ${user.name} (login: ${user.login})")
                onClickListener?.invoke(user)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_contributor

    override fun initializeViewBinding(view: View): ItemContributorBinding =
        ItemContributorBinding.bind(view)
}
