package io.benjamintan.ankogit.data.api

import io.benjamintan.ankogit.data.api.interceptors.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    val url = "https://api.github.com/"

    fun <T> create(serviceClass: Class<T>, baseUrl: String = url): T {
        val client = OkHttpClient.Builder().apply {
            interceptors().add(LoggingInterceptor())
        }.build()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        return retrofit.create(serviceClass)
    }
}
