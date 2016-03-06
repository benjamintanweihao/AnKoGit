package io.benjamintan.ankogit.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding.widget.RxTextView
import io.benjamintan.ankogit.R
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.subscribeWith

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login = find<EditText>(R.id.login)
        val password = find<EditText>(R.id.password)
        val signInBtn = find<Button>(R.id.sign_in_btn).apply {
            isEnabled = false
            setOnClickListener {
                doSignIn(login.text.toString(), password.text.toString())
            }
        }

        val loginNotBlank = notBlankObservable(login)
        val passwordNotBlank = notBlankObservable(password)

        Observable
                .combineLatest(loginNotBlank, passwordNotBlank, { l, p -> l && p })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith {
                    onNext {
                        signInBtn.isEnabled = it
                    }
                }
    }

    private fun notBlankObservable(login: EditText): Observable<Boolean>? {
        val loginNotBlank: Observable<Boolean>? = RxTextView
                .textChangeEvents(login)
                .map { it.text().isNotBlank() }
        return loginNotBlank
    }

    private fun doSignIn(login: String, password: String) {
        toast("$login:$password")
    }
}

