package io.benjamintan.ankogit.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import io.benjamintan.ankogit.App
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.TOKEN
import io.benjamintan.ankogit.USERNAME
import io.benjamintan.ankogit.adapters.EventListAdapter
import io.benjamintan.ankogit.data.api.GitHubService
import org.jetbrains.anko.find
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.toast
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.subscribeWith
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity() {

    @field:[Inject Named("io")]
    lateinit var schedulerIO: Scheduler

    @Inject
    lateinit var service: GitHubService

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).component(this).inject(this)

        val username = sharedPreferences.getString(USERNAME, "")
        val token = sharedPreferences.getString(TOKEN, "")

        service.getReceivedEvents(token, username)
                .subscribeOn(schedulerIO)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith {
                    onNext {
                        find<RecyclerView>(R.id.event_list).apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            adapter = EventListAdapter(it)
                        }
                    }

                    onError {
                        toast(it.getStackTraceString())
                    }
                }
    }
}
