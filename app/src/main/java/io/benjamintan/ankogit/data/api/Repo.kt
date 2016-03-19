package io.benjamintan.ankogit.data.api

data class Repo(
        val name: String,
        val description: String,
        val stargazers_count: Int = 0,
        val watchers_count: Int = 0,
        val language: String = "unknown",
        val url: String)
