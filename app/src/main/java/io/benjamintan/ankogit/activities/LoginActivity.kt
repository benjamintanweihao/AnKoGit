package io.benjamintan.ankogit.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import io.benjamintan.ankogit.App
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.TOKEN
import io.benjamintan.ankogit.USERNAME
import io.benjamintan.ankogit.data.api.GitHubAuth
import io.benjamintan.ankogit.data.api.GitHubService
import io.benjamintan.ankogit.utils.createNotBlankObservable
import org.jetbrains.anko.find
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import rx.Observable
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.subscribeWith
import javax.inject.Inject
import javax.inject.Named

class LoginActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_login)

        (application as App).component(this).inject(this)

        val login = find<EditText>(R.id.login)
        val password = find<EditText>(R.id.password)
        val loginNotBlankObs = login.createNotBlankObservable()
        val passwordNotBlankObs = password.createNotBlankObservable()

        val signInBtn = find<Button>(R.id.sign_in_btn).apply {
            isEnabled = false
            setOnClickListener {
                doSignIn(login.text.toString(), password.text.toString())
            }
        }

        Observable
                .combineLatest(loginNotBlankObs, passwordNotBlankObs, { l, p -> l && p })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith {
                    onNext {
                        signInBtn.isEnabled = it
                    }
                }
    }

    private fun doSignIn(login: String, password: String) {
        val authStr = "$login:$password"
        val encodedAuthStr = "Basic ${Base64.encodeToString(authStr.toByteArray(), Base64.NO_WRAP)}"

        service.createAuthorization(encodedAuthStr, authBody)
                .subscribeOn(schedulerIO)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith {
                    onNext {
                        when (it.code()) {
                            201 -> {
                                sharedPreferences
                                        .edit()
                                        .putString(USERNAME, login)
                                        .putString(TOKEN, "token ${it.body().token}")
                                        .commit()

                                startActivity<MainActivity>()
                            }
                            401 ->
                                if (!it.headers().get("X-GitHub-OTP").isNullOrEmpty()) {
                                    sharedPreferences
                                            .edit()
                                            .putString(USERNAME, login)
                                            .commit()

                                    startActivity<OTPActivity>("authString" to encodedAuthStr)
                                } else {
                                    toast(text = "Invalid username or password.")
                                }
                        }
                    }
                    onError {
                        toast(it.getStackTraceString())
                    }
                }
    }
}

