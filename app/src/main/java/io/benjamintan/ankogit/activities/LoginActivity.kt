package io.benjamintan.ankogit.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding.widget.RxTextView
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.data.api.GitHubService
import io.benjamintan.ankogit.data.api.ServiceGenerator
import org.jetbrains.anko.find
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.subscribeWith
import org.jetbrains.anko.*
import android.util.Base64

class LoginActivity : AppCompatActivity() {

    lateinit var service: GitHubService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        service = ServiceGenerator.create(GitHubService::class.java)

        val login = find<EditText>(R.id.login)
        val password = find<EditText>(R.id.password)
        val loginNotBlankObs = notBlankObservable(login)
        val passwordNotBlankObs = notBlankObservable(password)

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

        val response = service.login(encodedAuthStr).execute()
        if (response.isSuccess) {
            startActivity<MainActivity>()
        }
    }

    private fun notBlankObservable(login: EditText): Observable<Boolean>? {
        val loginNotBlank: Observable<Boolean>? = RxTextView
                .textChangeEvents(login)
                .map { it.text().isNotBlank() }
        return loginNotBlank
    }
}

