package io.benjamintan.ankogit.data.api

import okhttp3.MediaType
import okhttp3.RequestBody
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
    fun authorizations(@Body note: JSONObject): Observable<Authorization>

    @PUT("authorizations/clients/{client_id}")
    fun getOrCreateAuthorization(@Header("Authorization") authorization: String,
                                 @Path("client_id") clientId: String = clientId(),
                                 @Body body: RequestBody = defaultRequestBody()): Observable<Authorization>

    @PUT("authorizations/clients/{client_id}")
    fun getOrCreateAuthorization(@Header("Authorization") authorization: String,
                                 @Header("X-GitHub-OTP") otp: String,
                                 @Path("client_id") clientId: String = clientId(),
                                 @Body body: RequestBody = defaultRequestBody()): Observable<Authorization>

    private fun defaultRequestBody(): RequestBody {
        val mediaType = MediaType.parse("application/json");
        val clientSecret = clientSecret()
        return RequestBody.create(mediaType, "{\"scopes\":[\"repo\"], \"client_secret\": \"$clientSecret\" }");
    }

    private fun clientId(): String = "f7d35391104c7046d3c7"

    private fun clientSecret(): String = "bfed120ad50cb4c4e0d0100877414a5193bcee1a"
}

