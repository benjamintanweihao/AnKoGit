package io.benjamintan.ankogit.data.api

data class Repo(val name: String,
                val description: String,
                val stargazers_count: Int,
                val watchers_count: Int,
                val language: String)
