package io.benjamintan.ankogit.data.api

import android.util.Base64
import io.benjamintan.ankogit.APIServiceTestHelper
import io.benjamintan.ankogit.RobolectricTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExternalResource

import org.mockito.Matchers.*

class GitHubServiceTest : RobolectricTest() {

    val server = MockWebServer()
    var service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())

    @Rule @JvmField
    val resource = object : ExternalResource() {
        override fun after() = server.shutdown()
    }

    @Test
    fun test_GET_user_200() {
        val responseCode = 200

        val username = "username"
        val password = "password"
        val credentials = "$username:$password"
        val basic = "Basic ${Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)}"

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setBody(APIServiceTestHelper.body("GET", "user", responseCode)))

        val response = service.login(basic).execute()

        println(response.message())
        println(response.body())
    }

    @Test
    fun test_GET_user_401_wrong_password() {
        val responseCode = 401

        val username = "username"
        val password = "password"
        val credentials = "${username}:${password}"
        val basic = "Basic ${Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)}"

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setBody(APIServiceTestHelper.body("GET", "user", responseCode)))

        val response = service.login(basic).execute()

        println(response.code())
        println(response.message())
    }

    @Test
    fun test_GET_user_401_otp_required() {
        val responseCode = 401

        val username = "username"
        val password = "password"
        val credentials = "${username}:${password}"
        val basic = "Basic ${Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)}"

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setHeader("X-GitHub-OTP", "required; :2fa-type")
                .setBody(APIServiceTestHelper.body("GET", "user", responseCode)))

        val response = service.login(basic).execute()

        println(response.headers())
        println(response.code())
        println(response.message())
    }

    @Test
    fun test_GET_user_200_with_otp() {
        val responseCode = 200

        val username = "username"
        val password = "password"
        val credentials = "${username}:${password}"
        val basic = "Basic ${Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)}"
        val otp = "123456"

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setBody(APIServiceTestHelper.body("GET", "user", responseCode)))

        val response = service.login(basic, otp).execute()

        println(response.message())
        println(response.body())
    }

    @Test
    fun test_GET_users_user_repo_200() {
        val responseCode = 200

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setBody(APIServiceTestHelper.body("GET", "users_user_repo", responseCode)))
        val response = service.userRepos(anyString()).execute()
        println(response.body())
    }

    @Test
    fun test_POST_authorizations_201() {
        val responseCode = 201

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setBody(APIServiceTestHelper.body("POST", "authorizations", responseCode)))

        val json = JSONObject()
        json.put("note", "a note")

        val response = service.authorizations(json).execute()
        println(response.body())
    }

    @Test
    fun test_POST_authorizations_422() {
        val responseCode = 422

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setBody(APIServiceTestHelper.body("POST", "authorizations", responseCode)))

        val response = service.authorizations(JSONObject()).execute()
        println(response.body()?.token)
    }
}

