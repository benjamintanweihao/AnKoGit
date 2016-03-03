package io.benjamintan.ankogit.data.api

import org.junit.Test

class GitHubServiceTest {

    @Test
    fun test_list_repos() {
        val service = ServiceGenerator.create(GitHubService::class.java)
        val response = service.listRepos("benjamintanweihao").execute()

        for (r in response.body()) {
            println(r)
        }
    }
}

