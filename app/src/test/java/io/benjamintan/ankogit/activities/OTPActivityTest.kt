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
import org.robolectric.Robolectric
import org.robolectric.internal.ShadowExtractor
import org.robolectric.shadows.ShadowActivity

class OTPActivityTest : RobolectricTest() {

    val server = MockWebServer()

    @Test
    fun sign_in_button_should_enable_only_when_otp_is_filled() {

    }

    // TODO: Need to pass along the "Basic: auth" string over to the
    // TODO: next activity
    @Test
    fun sign_in_successful() {
        server.enqueue(MockResponse()
                .setResponseCode(200)
                .setBody(APIServiceTestHelper.body("GET", "user", 200)))

        val activity = Robolectric.setupActivity(OTPActivity::class.java).apply {
            service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
        }

        val shadowActivity = ShadowExtractor.extract(activity) as ShadowActivity

        activity.find<EditText>(R.id.otp).apply { setText("123456") }
        activity.find<Button>(R.id.sign_in_btn).apply { performClick() }

        val expectedIntent = Intent(activity, MainActivity::class.java)
        val actualIntent = shadowActivity.nextStartedActivity
        assert(expectedIntent.filterEquals(actualIntent))
    }

}

