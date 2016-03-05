package io.benjamintan.ankogit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import io.benjamintan.ankogit.R
import org.jetbrains.anko.find
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login = find<EditText>(R.id.login)
        val password = find<EditText>(R.id.password)
        val signInBtn = find<Button>(R.id.sign_in_btn)
        signInBtn.setOnClickListener {  }
    }
}

