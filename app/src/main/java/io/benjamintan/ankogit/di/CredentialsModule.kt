package io.benjamintan.ankogit.di

import android.content.Context
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Named
import javax.inject.Singleton
import io.benjamintan.ankogit.R
import io.benjamintan.ankogit.data.api.GitHubAuth
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

@Module
class CredentialsModule {

    @Provides
    @Singleton
    @Named("configProperties")
    fun configProperties(context: Context): Properties {
        val props = Properties()
        context.resources.openRawResource(R.raw.config).use {
            props.load(it)
        }
        return props
    }

    @Provides
    @Singleton
    @Named("githubClientId")
    fun githubClientId(@Named("configProperties") props: Properties): String {
        return props.getProperty("githubClientId", "no github client id supplied")
    }

    @Provides
    @Singleton
    @Named("githubClientSecret")
    fun githubClientSecret(@Named("configProperties") props: Properties): String {
        return props.getProperty("githubClientSecret", "no github client secret supplied")
    }

    @Provides
    @Singleton
    @Named("githubAuthBody")
    fun githubAuthJSONBody(@Named("githubClientId") clientId: String, @Named("githubClientSecret") clientSecret: String): GitHubAuth {
        return GitHubAuth(clientId, clientSecret)
    }
}

