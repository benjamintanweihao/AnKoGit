package io.benjamintan.ankogit

import java.io.File

object APIServiceTestHelper {

    fun body(fileName: String, responseCode: Int): String {
        val basePath = "src/test/java/io/benjamintan/ankogit/assets"
        return File("$basePath/${fileName}_$responseCode.json").readText()
    }
}

