package io.benjamintan.ankogit.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.benjamintan.ankogit.data.api.Event

class EventListAdapter(val events: List<Event> = emptyList()) : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        throw UnsupportedOperationException()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        throw UnsupportedOperationException()
    }

    override fun getItemCount(): Int = events.count()

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}

