package com.constantinemars.okhttpcachingpoc

import com.constantinemars.okhttpcachingpoc.model.GetResponse
import com.constantinemars.okhttpcachingpoc.model.PostResponse
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("get")
    fun get(): Flowable<GetResponse>

    @POST("post")
    fun post(@Body data: String): Flowable<PostResponse>
}