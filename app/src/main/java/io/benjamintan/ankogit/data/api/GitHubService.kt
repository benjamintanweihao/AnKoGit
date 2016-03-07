package io.benjamintan.ankogit.data.api

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import rx.Observable

interface GitHubService {
    @GET("users/{user}/repos")
    fun userRepos(@Path("user") user: String): Observable<List<Repo>>

    @GET("user")
    fun login(@Header("Authorization") authorization: String): Observable<User>

    @GET("user")
    fun login(@Header("Authorization") authorization: String,
              @Header("X-GitHub-OTP") otp: String): Observable<User>

    @POST("authorizations")
    fun authorizations(@Body note: JSONObject) : Observable<Authorization>
}

