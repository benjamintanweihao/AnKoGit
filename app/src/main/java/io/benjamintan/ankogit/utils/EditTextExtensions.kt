package io.benjamintan.ankogit.utils

import android.widget.EditText
import com.jakewharton.rxbinding.widget.RxTextView
import rx.Observable

fun EditText.createNotBlankObservable(): Observable<Boolean>? {
    return RxTextView
            .textChangeEvents(this)
            .map { it.text().isNotBlank() }
}