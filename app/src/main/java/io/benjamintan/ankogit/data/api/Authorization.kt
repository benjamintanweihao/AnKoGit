package io.benjamintan.ankogit.data.api

data class Authorization(
        val id: Double,
        val url: String,
        val token: String,
        val hashed_token: String,
        val note: String
)
