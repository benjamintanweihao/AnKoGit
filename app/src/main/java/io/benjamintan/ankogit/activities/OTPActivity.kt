package io.benjamintan.ankogit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import io.benjamintan.ankogit.App
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.data.api.GitHubService
import io.benjamintan.ankogit.utils.createNotBlankObservable
import okhttp3.RequestBody
import org.jetbrains.anko.*
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

    @field:[Inject Named("githubRequestBody")]
    lateinit var requestBody: RequestBody

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
                service
                        .getOrCreateAuthorization(basicAuthString, otp.text.toString(), clientId, requestBody)
                        .subscribeOn(schedulerIO)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith {
                            onNext {
                                startActivity<MainActivity>("hashedToken" to it.body().hashed_token)
                            }
                            onError {
                                toast(it.getStackTraceString())
                            }
                        }
            }
        }
    }
}
