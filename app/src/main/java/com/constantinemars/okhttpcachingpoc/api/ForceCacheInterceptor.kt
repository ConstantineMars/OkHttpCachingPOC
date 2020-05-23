package com.constantinemars.okhttpcachingpoc.api

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class ForceCacheInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .cacheControl(CacheControl.FORCE_CACHE)
        return chain.proceed(builder.build())
    }
}