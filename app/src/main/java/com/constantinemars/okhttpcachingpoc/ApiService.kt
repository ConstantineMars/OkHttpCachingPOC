package com.constantinemars.okhttpcachingpoc

import com.constantinemars.okhttpcachingpoc.model.PostmanEchoResponse
import io.reactivex.Flowable
import retrofit2.http.GET

interface ApiService {
    @GET("get")
    fun get(): Flowable<PostmanEchoResponse>
}