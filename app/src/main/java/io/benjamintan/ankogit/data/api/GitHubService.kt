package io.benjamintan.ankogit.data.api

import retrofit2.Response
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
    fun createAuthorization(
            @Header("Authorization") authorization: String,
            @Body body: GitHubAuth): Observable<Response<Authorization>>

    @POST("authorizations")
    fun createAuthorization(
            @Header("Authorization") authorization: String,
            @Header("X-GitHub-OTP") otp: String,
            @Body body: GitHubAuth): Observable<Response<Authorization>>

    @GET("users/{username}/received_events")
    fun getReceivedEvents(@Header("Authorization") authorization: String,
                          @Path("username") userName: String): Observable<List<Event>>
}

