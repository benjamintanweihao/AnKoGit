package io.benjamintan.ankogit.activities

import android.content.Intent
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import io.benjamintan.ankogit.APIServiceTestHelper
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.RobolectricTest
import io.benjamintan.ankogit.data.api.GitHubService
import io.benjamintan.ankogit.data.api.ServiceGenerator
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.jetbrains.anko.find
import org.junit.Test
import org.mockito.Mockito.*
import org.robolectric.Robolectric
import org.robolectric.Shadows.*
import org.robolectric.internal.ShadowExtractor
import org.robolectric.shadows.ShadowActivity
import rx.schedulers.Schedulers

class LoginActivityTest : RobolectricTest() {

    val server = MockWebServer()
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

    @Test
    fun sign_in_successful() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(APIServiceTestHelper.body("GET", "user", 200)))

        val activity = Robolectric.setupActivity(LoginActivity::class.java).apply {
            service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
            schedulerIO = Schedulers.immediate()
        }

        val shadowActivity = ShadowExtractor.extract(activity) as ShadowActivity

        activity.find<EditText>(R.id.login).apply { setText("username") }
        activity.find<EditText>(R.id.password).apply { setText("password") }
        activity.find<Button>(R.id.sign_in_btn).apply { performClick() }

        val expectedIntent = Intent(activity, MainActivity::class.java)
        val actualIntent = shadowActivity.nextStartedActivity
        assert(expectedIntent.filterEquals(actualIntent))
    }

    @Test
    fun sign_in_unsuccessful_needs_otp() {
        val responseCode = 401

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setHeader("X-GitHub-OTP", "required; :2fa-type")
                .setBody(APIServiceTestHelper.body("GET", "user", responseCode)))

        val activity = Robolectric.setupActivity(LoginActivity::class.java).apply {
            service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
            schedulerIO = Schedulers.immediate()
        }

        val shadowActivity = ShadowExtractor.extract(activity) as ShadowActivity

        activity.find<EditText>(R.id.login).apply { setText("username") }
        activity.find<EditText>(R.id.password).apply { setText("password") }
        activity.find<Button>(R.id.sign_in_btn).apply { performClick() }

        val expectedIntent = Intent(activity, OTPActivity::class.java)
        val actualIntent = shadowActivity.nextStartedActivity

        assert(expectedIntent.filterEquals(actualIntent))
    }
}

