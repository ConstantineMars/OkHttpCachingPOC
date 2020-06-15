package com.constantinemars.okhttpcachingpoc.api.cache

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import okio.BufferedSource
import java.io.ByteArrayInputStream
import java.util.concurrent.TimeUnit

class FakeResponseBody(val data: String): ResponseBody() {
    override fun contentLength(): Long {
        return data.length.toLong()
    }

    override fun contentType(): MediaType? {
        return "text/plain".toMediaType()
    }

    override fun source(): BufferedSource {
        val buffer = Buffer()
        buffer.inputStream().read(data.toByteArray())
        return buffer
    }

}

class ForceCacheInterceptor: BaseCacheInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if("POST" == request.method.toUpperCase()) {

            val body = FakeResponseBody("test data")
            val response = Response.Builder()
                .body(body)
                .build()

            return response
        }

        val builder = request.newBuilder()
            .cacheControl(CacheControl.Builder()
                .onlyIfCached()
                .maxStale(30, TimeUnit.SECONDS) // same as CacheControl.FORCE_CACHE but with max-stale limited to 1 day
                .build())
        return chain.proceed(builder.build())
    }
}