package io.benjamintan.ankogit

import android.app.Application
import android.content.Context
import io.benjamintan.ankogit.di.*

class App : Application() {

    lateinit private var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .credentialsModule(CredentialsModule())
                .serviceModule(ServiceModule())
                .build()
    }

    fun component(context: Context): AppComponent {
        return (context.applicationContext as App).component
    }
}

