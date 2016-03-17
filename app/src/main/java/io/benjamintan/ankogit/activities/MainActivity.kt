package io.benjamintan.ankogit.activities

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import io.benjamintan.ankogit.App
import io.benjamintan.ankogit.R
import org.jetbrains.anko.find

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as App).component(this).inject(this)

        val hashedToken: String = intent.extras.getString("hashedToken", "noHashToken")

        find<TextView>(R.id.hashed_token).apply {
            text = hashedToken
        }
    }
}
