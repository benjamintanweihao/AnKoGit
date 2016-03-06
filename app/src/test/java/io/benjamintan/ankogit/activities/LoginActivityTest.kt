package io.benjamintan.ankogit.activities

import android.view.View
import android.widget.Button
import android.widget.EditText
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.RobolectricTest
import org.jetbrains.anko.find
import org.junit.Test
import org.robolectric.Robolectric

class LoginActivityTest : RobolectricTest() {

    val isEnabled = Matcher(View::isEnabled)

    @Test
    fun sign_in_button_should_enable_only_when_fields_are_filled() {
        val activity = Robolectric.setupActivity(LoginActivity::class.java)
        val login = activity.find<EditText>(R.id.login)
        val password = activity.find<EditText>(R.id.password)
        val signInButton = activity.find<Button>(R.id.sign_in_btn)

        assertThat(login.text.toString(), String::isEmpty)
        assertThat(password.text.toString(), String::isEmpty)
        assertThat(signInButton, !isEnabled)

        login.setText("login")
        assertThat(signInButton, !isEnabled)

        password.setText("sekret")
        assertThat(signInButton, isEnabled)
    }
}

