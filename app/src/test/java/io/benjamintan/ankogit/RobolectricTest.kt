package io.benjamintan.ankogit

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
class RobolectricTest {

    @Test
    fun test_itShouldDoSomething() {
        assertEquals(4, 2 + 2);
    }
}
