package io.benjamintan.ankogit.data.api

import io.benjamintan.ankogit.APIServiceTestHelper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test

import org.mockito.Matchers.*

class GitHubServiceTest {

    val server = MockWebServer()

    @Before
    fun setup() {
        server.start()
    }

    @Test
    fun test_list_repos() {
        val fileName = "list_repos"
        val responseCode = 200

        server.enqueue(MockResponse()
                .setResponseCode(responseCode)
                .setBody(APIServiceTestHelper.body(fileName, responseCode)))

        val service = ServiceGenerator.create(GitHubService::class.java, server.url("").toString())
        val response = service.listRepos(anyString()).execute()
        println(response.body())
    }

}

