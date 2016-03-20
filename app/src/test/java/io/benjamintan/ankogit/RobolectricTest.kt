package io.benjamintan.ankogit

import android.content.Context
import org.junit.Before
import org.junit.Ignore
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(21))
@Ignore open class RobolectricTest {

    @Before
    open fun setup() {
        ShadowLog.stream = System.out;
    }

    fun context() : Context = RuntimeEnvironment.application
}
