package io.benjamintan.ankogit.adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.benjamintan.ankogit.RobolectricTest
import io.benjamintan.ankogit.data.api.Actor
import io.benjamintan.ankogit.data.api.Event
import io.benjamintan.ankogit.data.api.Repo
import kotlinx.android.synthetic.main.event.view.*
import org.junit.Before
import org.junit.Test
import java.util.*

class EventListAdapterTest : RobolectricTest() {

    lateinit var e: Event

    @Before
    override fun setup() {
        super.setup()

        e = Event(type = "",
                actor = Actor("benjamintanweihao", "url", "avatar"),
                repo = Repo(name = "ankogit/ankogit", url = "http://www.example.com"),
                created_at = Date())
    }

    @Test
    fun initialized_with_events() {
        val events = listOf(e, e, e)
        val adapter = EventListAdapter(events)

        assertThat(adapter.itemCount, equalTo(3))
    }

    @Test
    fun view_holder_watch_event() {
        val watchEvent = e.copy(type = "WatchEvent")
        val viewHolder = setupViewHolderWithEvent(watchEvent)

        assertThat(viewHolder.whoAndActionTextView.text.toString(), equalTo("benjamintanweihao starred"))
        assertThat(viewHolder.repoAndWhenAgoTextView.text.toString(), equalTo("ankogit/ankogit"))
    }

    @Test
    fun view_holder_create_event() {
        val createEvent = e.copy(type = "CreateEvent")
        val viewHolder = setupViewHolderWithEvent(createEvent)

        assertThat(viewHolder.whoAndActionTextView.text.toString(), equalTo("benjamintanweihao created"))
        assertThat(viewHolder.repoAndWhenAgoTextView.text.toString(), equalTo("ankogit/ankogit"))
    }

    @Test
    fun view_holder_member_event() {
        val memberEvent = e.copy(type = "MemberEvent")
        val viewHolder = setupViewHolderWithEvent(memberEvent)

        assertThat(viewHolder.whoAndActionTextView.text.toString(), equalTo("benjamintanweihao joined"))
        assertThat(viewHolder.repoAndWhenAgoTextView.text.toString(), equalTo("ankogit/ankogit"))
    }

    @Test
    fun view_holder_fork_event() {
        val memberEvent = e.copy(type = "ForkEvent")
        val viewHolder = setupViewHolderWithEvent(memberEvent)

        assertThat(viewHolder.whoAndActionTextView.text.toString(), equalTo("benjamintanweihao forked"))
        assertThat(viewHolder.repoAndWhenAgoTextView.text.toString(), equalTo("ankogit/ankogit"))
    }

    @Test
    fun view_holder_unknown_event() {
        val anyOtherEvent = e.copy(type = "ANYTHING")
        val viewHolder = setupViewHolderWithEvent(anyOtherEvent)

        assertThat(viewHolder.whoAndActionTextView.text.toString(), equalTo("benjamintanweihao "))
        assertThat(viewHolder.repoAndWhenAgoTextView.text.toString(), equalTo("ankogit/ankogit"))
    }

    private fun setupViewHolderWithEvent(event: Event): EventListAdapter.ViewHolder {
        val parent = RecyclerView(context()).apply {
            layoutManager = LinearLayoutManager(context())
        }
        val adapter = EventListAdapter(events = listOf(event))
        val viewHolder = adapter.onCreateViewHolder(parent, viewType = -1)
        adapter.onBindViewHolder(viewHolder, 0)

        return viewHolder
    }
}

