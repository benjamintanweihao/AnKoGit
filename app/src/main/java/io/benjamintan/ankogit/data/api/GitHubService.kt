package io.benjamintan.ankogit.data.api

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface GitHubService {
    @GET("users/{user}/repos")
    fun userRepos(@Path("user") user: String): Call<List<Repo>>

    @GET("user")
    fun login(@Header("Authorization") authorization: String): Call<User>

    @GET("user")
    fun login(@Header("Authorization") authorization: String,
              @Header("X-GitHub-OTP") otp: String): Call<User>

    @POST("authorizations")
    fun authorizations(@Body note: JSONObject) : Call<Authorization>

}

