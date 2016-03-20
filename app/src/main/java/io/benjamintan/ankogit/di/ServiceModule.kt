package io.benjamintan.ankogit.di

import dagger.Module
import dagger.Provides
import io.benjamintan.ankogit.data.api.GitHubService
import io.benjamintan.ankogit.data.api.ServiceGenerator
import javax.inject.Singleton

@Module
class ServiceModule {

    @Provides
    @Singleton
    fun githubService(): GitHubService {
        return ServiceGenerator.create(GitHubService::class.java)
    }
}

