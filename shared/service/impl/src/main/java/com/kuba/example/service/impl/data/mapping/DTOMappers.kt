package com.kuba.example.service.impl.data.mapping

import com.kuba.example.service.api.Repository
import com.kuba.example.service.api.User
import com.kuba.example.service.api.UserDetails
import com.kuba.example.service.impl.data.api.dto.ContributorDTO
import com.kuba.example.service.impl.data.api.dto.RepoDTO
import com.kuba.example.service.impl.data.api.dto.UserDTO


fun UserDTO.mapToUserDetails(): UserDetails = UserDetails(
    login = login,
    name = name,
    email = email,
    followers = followers,
    following = following,
    location = location,
    avatarUrl = avatarUrl
)


fun RepoDTO.mapToRepository(): Repository =
    Repository(
        id = id,
        name = name,
        description = description,
        ownerLogin = owner.login,
        stars = stars
    )


fun ContributorDTO.mapToUser() = User(
    login = login, name = null, avatarUrl = avatarUrl
)
