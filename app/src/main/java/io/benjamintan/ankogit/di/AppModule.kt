package io.benjamintan.ankogit.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.provider.SyncStateContract
import dagger.Module
import dagger.Provides
import io.benjamintan.ankogit.PREFERENCES
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(application: Application) {
    private var application: Application

    init {
        this.application = application
    }

    @Provides
    @Singleton
    fun context(): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    @Named("immediate")
    fun schedulerImmediate(): Scheduler {
        return Schedulers.immediate();
    }

    @Provides
    @Singleton
    @Named("io")
    fun schedulerIO(): Scheduler {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    @Named("mainThread")
    fun schedulerMainThread(): Scheduler {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    fun sharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }
}

