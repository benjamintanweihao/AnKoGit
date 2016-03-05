package io.benjamintan.ankogit.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    val url = "https://api.github.com"

    fun <T> create(serviceClass: Class<T>, baseUrl: String = url): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit.create(serviceClass)
    }

}
