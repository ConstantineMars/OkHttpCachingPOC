package com.constantinemars.okhttpcachingpoc.api.cache

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class NoCacheInterceptor: BaseCacheInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .cacheControl(CacheControl.FORCE_NETWORK)
        return chain.proceed(builder.build())
    }
}