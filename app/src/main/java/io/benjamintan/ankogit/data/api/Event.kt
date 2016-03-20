package io.benjamintan.ankogit.data.api

import java.util.*

data class Event(
        val type: String,
        val actor: Actor,
        val repo: Repo,
        val created_at: Date)
