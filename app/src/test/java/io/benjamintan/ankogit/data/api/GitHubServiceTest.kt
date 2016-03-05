package io.benjamintan.ankogit.data.api

import io.benjamintan.ankogit.APIServiceTestHelper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName

import org.mockito.Matchers.*

class GitHubServiceTest {

    val server = MockWebServer()
    var methodName: String = ""

    @Rule @JvmField
    var name = TestName()

    @Before
    fun setup() {
        server.start()
        methodName = name.methodName.split("test_")[1]
    }

    @Test
    fun test_list_repos() {
        val responseCode = 200

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setBody(APIServiceTestHelper.body(methodName, responseCode)))

        val service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
        val response = service.listRepos(anyString()).execute()
        println(response.body())
    }

}

