package com.constantinemars.okhttpcachingpoc.api.cache

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class ForceCacheInterceptor: BaseCacheInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .cacheControl(CacheControl.Builder()
                .onlyIfCached()
                .maxStale(30, TimeUnit.SECONDS) // same as CacheControl.FORCE_CACHE but with max-stale limited to 1 day
                .build())
        return chain.proceed(builder.build())
    }
}