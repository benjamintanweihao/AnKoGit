package io.benjamintan.ankogit.data.api

data class User(
        val id: Long,
        val login: String,
        val avatar_url: String,
        val name: String,
        val public_repos: Int,
        val followers: Int,
        val following: Int
)
