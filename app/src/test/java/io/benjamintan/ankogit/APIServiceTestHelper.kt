package io.benjamintan.ankogit

import java.io.File

object APIServiceTestHelper {

    val basePath = "src/test/java/io/benjamintan/ankogit/assets"

    fun body(httpVerb: String, endpoint: String, responseCode: Int): String {
        return File("$basePath/${httpVerb}_${endpoint}_${responseCode}.json").readText()
    }
}

