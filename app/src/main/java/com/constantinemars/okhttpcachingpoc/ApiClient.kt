package com.constantinemars.okhttpcachingpoc

import com.constantinemars.okhttpcachingpoc.api.ForceCacheInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.IOException

class ApiClient {
    companion object {
        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://postman-echo.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        }

        private val okHttpClient by lazy {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor())
                .cache(cache)
                .addInterceptor(ForceCacheInterceptor())
                .build()
        }

        private val cache by lazy {
            try {
                val cacheSize = 10 * 1024 * 1024L // 10 MiB
                Cache(App.instance.cacheDir, cacheSize)
            } catch (e: IOException) {
                Timber.e(e, "Could not set cache")
                null
            }
        }
    }
}