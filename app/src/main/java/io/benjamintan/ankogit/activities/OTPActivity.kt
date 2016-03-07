package io.benjamintan.ankogit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.data.api.GitHubService
import io.benjamintan.ankogit.data.api.ServiceGenerator
import org.jetbrains.anko.*
import rx.lang.kotlin.subscribeWith

class OTPActivity : AppCompatActivity() {

    lateinit var service: GitHubService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        service = ServiceGenerator.create(GitHubService::class.java)

        val otp = find<EditText>(R.id.otp)
        val signInBtn = find<Button>(R.id.sign_in_btn)

        signInBtn.setOnClickListener {
            service
                    .login("basic auth string", otp.text.toString())
                    .subscribeWith {
                        onNext { startActivity<MainActivity>() }
                        onError { toast(it.message.toString()) }
                    }
        }
    }
}
