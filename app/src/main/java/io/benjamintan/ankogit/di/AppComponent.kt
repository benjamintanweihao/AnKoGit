package io.benjamintan.ankogit.di

import dagger.Component
import io.benjamintan.ankogit.App
import io.benjamintan.ankogit.activities.LoginActivity
import io.benjamintan.ankogit.activities.MainActivity
import io.benjamintan.ankogit.activities.OTPActivity
import javax.inject.Singleton

@Component(
        modules = arrayOf(AppModule::class)
)
@Singleton
interface AppComponent {
    fun inject(application: App)

    fun inject(activity: LoginActivity)

    fun inject(activity: OTPActivity)

    fun inject(activity: MainActivity)
}
