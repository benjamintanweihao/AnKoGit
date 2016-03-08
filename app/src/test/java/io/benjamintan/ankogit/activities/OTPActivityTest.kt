package io.benjamintan.ankogit.activities

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import io.benjamintan.ankogit.APIServiceTestHelper
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.RobolectricTest
import io.benjamintan.ankogit.data.api.GitHubService
import io.benjamintan.ankogit.data.api.ServiceGenerator
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.jetbrains.anko.find
import org.junit.Test
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.internal.ShadowExtractor
import org.robolectric.shadows.ShadowActivity

class OTPActivityTest : RobolectricTest() {

    val server = MockWebServer()

    @Test
    fun sign_in_button_should_enable_only_when_otp_is_filled() {

    }

    @Test
    fun successful_sign_in_goes_to_main_screen() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(APIServiceTestHelper.body("GET", "user", 200)))

        val basicAuth = "Basic: xxx"
        val otp = "123456"
        val intent = Intent().apply { putExtra("authString", basicAuth) }

        val activity = Robolectric.buildActivity(OTPActivity::class.java).withIntent(intent).create().get().apply {
            service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
        }

        val shadowActivity = ShadowExtractor.extract(activity) as ShadowActivity

        activity.find<EditText>(R.id.otp).apply { setText(otp) }
        activity.find<Button>(R.id.sign_in_btn).apply { performClick() }

        val expectedIntent = Intent(activity, MainActivity::class.java)
        val actualIntent = shadowActivity.nextStartedActivity
        assert(expectedIntent.filterEquals(actualIntent))
    }

    // TODO: How to test that the right auth string and OTP got passed in?
    @Test
    fun basic_auth_string_and_otp_passed_into_service() {
        val basicAuth = "Basic: xxx"
        val otp = "123456"
        val intent = Intent().apply { putExtra("authString", basicAuth) }

        val activity = Robolectric.buildActivity(OTPActivity::class.java).withIntent(intent).create().get().apply {
            service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
        }

        activity.find<EditText>(R.id.otp).apply { setText(otp) }
        activity.find<Button>(R.id.sign_in_btn).apply { performClick() }

        verify(activity.service).login(basicAuth, otp)
    }

}

