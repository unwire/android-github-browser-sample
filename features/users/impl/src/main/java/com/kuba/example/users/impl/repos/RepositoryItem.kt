package com.kuba.example.users.impl.repos

import android.view.View
import com.kuba.example.users.impl.R
import com.kuba.example.users.impl.databinding.ItemRepositoryBinding
import com.xwray.groupie.viewbinding.BindableItem
import timber.log.Timber

data class RepositoryItem(
    val owner: String,
    val name: String,
    val description: String?,
    val stars: Int
) : BindableItem<ItemRepositoryBinding>() {

    var onClickListener: ((ownerLogin: String, name: String, description: String?) -> Unit)? = null

    override fun bind(binding: ItemRepositoryBinding, position: Int) {
        with(binding) {
            lblName.text = name
            lblDescription.text = description
            lblStars.text =
                if (stars < 1000) {
                    stars.toString()
                } else {
                    val kStars = stars / 1000
                    "${kStars}k"
                }

            root.setOnClickListener {
                Timber.d("Clicking repo $name owned by $owner")
                onClickListener?.invoke(owner, name, description)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_repository

    override fun initializeViewBinding(view: View): ItemRepositoryBinding =
        ItemRepositoryBinding.bind(view)
}