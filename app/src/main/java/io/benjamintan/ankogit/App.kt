package io.benjamintan.ankogit

import android.app.Application
import android.content.Context
import io.benjamintan.ankogit.di.AppComponent
import io.benjamintan.ankogit.di.AppModule
import io.benjamintan.ankogit.di.DaggerAppComponent
import io.benjamintan.ankogit.di.ServiceModule

class App : Application() {

    lateinit private var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .serviceModule(ServiceModule())
                .build()
    }

    fun component(context: Context): AppComponent {
        return (context.applicationContext as App).component
    }
}

