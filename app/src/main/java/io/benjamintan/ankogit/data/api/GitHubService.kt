package io.benjamintan.ankogit.data.api

import okhttp3.RequestBody
import org.json.JSONObject
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
    fun authorizations(@Body note: JSONObject): Observable<Authorization>

    @PUT("authorizations/clients/{client_id}")
    fun getOrCreateAuthorization(@Header("Authorization") authorization: String,
                                 @Path("client_id") clientId: String,
                                 @Body body: RequestBody): Observable<Response<Authorization>>

    @PUT("authorizations/clients/{client_id}")
    fun getOrCreateAuthorization(@Header("Authorization") authorization: String,
                                 @Header("X-GitHub-OTP") otp: String,
                                 @Path("client_id") clientId: String,
                                 @Body body: RequestBody): Observable<Response<Authorization>>

    @GET("users/{username}/received_events")
    fun getReceivedEvents(@Header("Authorization") authorization: String,
                          @Path("username") userName: String)
}

