package io.benjamintan.ankogit.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import io.benjamintan.ankogit.App
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.TOKEN
import io.benjamintan.ankogit.data.api.GitHubAuth
import io.benjamintan.ankogit.data.api.GitHubService
import io.benjamintan.ankogit.utils.createNotBlankObservable
import okhttp3.RequestBody
import org.jetbrains.anko.*
import org.json.JSONObject
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.subscribeWith
import javax.inject.Inject
import javax.inject.Named

class OTPActivity : AppCompatActivity() {

    @field:[Inject Named("io")]
    lateinit var schedulerIO: Scheduler

    @Inject
    lateinit var service: GitHubService

    @field:[Inject Named("githubClientId")]
    lateinit var clientId: String

    @field:[Inject Named("githubAuthBody")]
    lateinit var authBody: GitHubAuth

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        (application as App).component(this).inject(this)

        val basicAuthString: String = intent.extras.getString("authString", "noAuthStringKey")

        val signInBtn = find<Button>(R.id.sign_in_btn)
        val otp = find<EditText>(R.id.otp).apply {
            createNotBlankObservable()!!
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith {
                        onNext {
                            signInBtn.isEnabled = it
                        }
                    }
        }

        signInBtn.apply {
            setOnClickListener {
                service.createAuthorization(basicAuthString, otp.text.toString(), authBody)
                        .subscribeOn(schedulerIO)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith {
                            onNext {
                                sharedPreferences
                                        .edit()
                                        .putString(TOKEN, "token ${it.body().token}")
                                        .commit()

                                startActivity<MainActivity>()
                            }
                            onError {
                                toast(it.getStackTraceString())
                            }
                        }
            }
        }
    }
}
