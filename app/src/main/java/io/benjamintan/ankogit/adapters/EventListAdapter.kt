package io.benjamintan.ankogit.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.data.api.Event
import org.jetbrains.anko.find

class EventListAdapter(val events: List<Event> = emptyList()) : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.event, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        var actor = event.actor
        val userName = actor.login
        val eventVerb = when (event.type) {
            "WatchEvent" -> "starred"
            "CreateEvent" -> "created"
            "MemberEvent" -> "joined"
            "ForkEvent" -> "forked"
            else -> ""
        }
        val repo = event.repo.name

        holder.whoAndActionTextView.text = "$userName $eventVerb"
        holder.repoAndWhenAgoTextView.text = repo
        Glide.with(holder.itemView.context)
                .load(actor.avatar_url)
                .into(holder.iconImageView)
    }

    override fun getItemCount(): Int = events.count()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var iconImageView: ImageView
        internal var whoAndActionTextView: TextView
        internal var repoAndWhenAgoTextView: TextView

        init {
            iconImageView = itemView.find<ImageView>(R.id.icon)
            whoAndActionTextView = itemView.find<TextView>(R.id.who_and_action)
            repoAndWhenAgoTextView = itemView.find<TextView>(R.id.repo_and_when)
        }
    }
}

