package io.benjamintan.ankogit.activities

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmptyString
import io.benjamintan.ankogit.*
import io.benjamintan.ankogit.data.api.GitHubService
import io.benjamintan.ankogit.data.api.ServiceGenerator
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.jetbrains.anko.find
import org.junit.Test
import org.robolectric.Robolectric
import org.robolectric.internal.ShadowExtractor
import org.robolectric.shadows.ShadowActivity
import rx.schedulers.Schedulers

class OTPActivityTest : RobolectricTest() {

    val server = MockWebServer()
    val isEnabled = Matcher(View::isEnabled)

    @Test
    fun sign_in_button_should_enable_only_when_otp_is_filled() {
        val intent = Intent().apply { putExtra("authString", "Basic: xxx") }
        val activity = Robolectric.buildActivity(OTPActivity::class.java).withIntent(intent).create().get().apply {
            service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
        }

        val otp = activity.find<EditText>(R.id.otp)
        val signInButton = activity.find<Button>(R.id.sign_in_btn)

        assertThat(otp.text.toString(), String::isEmpty)
        assertThat(signInButton, !isEnabled)

        otp.setText("xxxxxx")
        assertThat(signInButton, isEnabled)
    }

    @Test
    fun successful_sign_in_goes_to_main_screen() {
        val responseCode = 201

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setHeader("X-GitHub-OTP", "required; :2fa-type")
                .setBody(APIServiceTestHelper.body("PUT", "authorizations_clients_client_id", responseCode)))

        val otp = "123456"
        val intent = Intent().apply { putExtra("authString", "Basic: xxx") }
        val activity = Robolectric.buildActivity(OTPActivity::class.java).withIntent(intent).create().get().apply {
            service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
            schedulerIO = Schedulers.immediate()
        }

        val shadowActivity = ShadowExtractor.extract(activity) as ShadowActivity

        activity.find<EditText>(R.id.otp).apply { setText(otp) }
        activity.find<Button>(R.id.sign_in_btn).apply { performClick() }

        val expectedIntent = Intent(activity, MainActivity::class.java)
        val actualIntent = shadowActivity.nextStartedActivity
        assert(expectedIntent.filterEquals(actualIntent))
    }

    @Test
    fun basic_auth_string_and_otp_passed_into_service() {
        val basicAuth = "Basic: xxxxxx"
        val otp = "123456"
        val intent = Intent().apply { putExtra("authString", basicAuth) }
        val activity = Robolectric.buildActivity(OTPActivity::class.java).withIntent(intent).create().get().apply {
            service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
            schedulerIO = Schedulers.immediate()
        }

        activity.find<EditText>(R.id.otp).apply { setText(otp) }
        activity.find<Button>(R.id.sign_in_btn).apply { performClick() }

        val expectedRequest = server.takeRequest();
        assertThat(expectedRequest.getHeader("Authorization"), equalTo(basicAuth));
        assertThat(expectedRequest.getHeader("X-GitHub-OTP"), equalTo(otp));
    }

    @Test
    fun successful_sign_in_stores_token_into_shared_preferences() {
        val responseCode = 201

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setHeader("X-GitHub-OTP", "required; :2fa-type")
                .setBody(APIServiceTestHelper.body("PUT", "authorizations_clients_client_id", responseCode)))

        val otp = "123456"
        val intent = Intent().apply { putExtra("authString", "Basic: xxx") }
        val activity = Robolectric.buildActivity(OTPActivity::class.java).withIntent(intent).create().get().apply {
            service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
            schedulerIO = Schedulers.immediate()
            sharedPreferences = context().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        }

        activity.find<EditText>(R.id.otp).apply { setText(otp) }
        activity.find<Button>(R.id.sign_in_btn).apply { performClick() }

        assertThat(activity.sharedPreferences.getString(TOKEN, ""), !isEmptyString)
    }

    @Test
    fun invalid_OTP_does_not_store_token_into_shared_preferences() {
        val responseCode = 401

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setHeader("X-GitHub-OTP", "anything")
                .setBody(APIServiceTestHelper.body("PUT", "authorizations_clients_client_id", responseCode)))

        val otp = "INVALID"
        val intent = Intent().apply { putExtra("authString", "Basic: xxx") }
        val activity = Robolectric.buildActivity(OTPActivity::class.java).withIntent(intent).create().get().apply {
            service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
            schedulerIO = Schedulers.immediate()
            sharedPreferences = context().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        }

        activity.find<EditText>(R.id.otp).apply { setText(otp) }
        activity.find<Button>(R.id.sign_in_btn).apply { performClick() }

        assertThat(activity.sharedPreferences.getString(TOKEN, ""), isEmptyString)
    }
}

