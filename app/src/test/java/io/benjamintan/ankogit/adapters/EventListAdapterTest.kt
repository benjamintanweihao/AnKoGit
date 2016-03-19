package io.benjamintan.ankogit.adapters

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.benjamintan.ankogit.RobolectricTest
import io.benjamintan.ankogit.data.api.Actor
import io.benjamintan.ankogit.data.api.Event
import io.benjamintan.ankogit.data.api.Repo
import org.junit.Test
import java.util.*

class EventListAdapterTest : RobolectricTest() {

    @Test
    fun initialized_with_events() {

        val e = Event("WatchEvent",
                Actor("login", "url", "avatar"),
                Repo("ankogit", "ankogit", url = "http://www.example.com"),
                Date())

        val events = listOf(e, e, e)
        val adapter = EventListAdapter(events)

        assertThat(adapter.itemCount, equalTo(3))
    }
}

