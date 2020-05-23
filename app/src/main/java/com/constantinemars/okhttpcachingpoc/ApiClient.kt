package com.constantinemars.okhttpcachingpoc

import com.constantinemars.okhttpcachingpoc.api.cache.BaseCacheInterceptor
import com.constantinemars.okhttpcachingpoc.api.cache.ForceCacheInterceptor
import com.constantinemars.okhttpcachingpoc.api.cache.NoCacheInterceptor
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
        val cachedRetrofit: Retrofit by lazy {
            getRetrofit(cachedOkHttpClient)
        }

        val noCacheRetrofit: Retrofit by lazy {
            getRetrofit(noCacheOkHttpClient)
        }

        private fun getRetrofit(okHttpClient: OkHttpClient) = run {
            Retrofit.Builder()
                .baseUrl("https://postman-echo.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        }

        private val cachedOkHttpClient by lazy {
            getOkHttpClient(ForceCacheInterceptor())
        }

        private val noCacheOkHttpClient by lazy {
            getOkHttpClient(NoCacheInterceptor())
        }

        private fun getOkHttpClient(cacheInterceptor: BaseCacheInterceptor) = run {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor())
                    .cache(cache)
                    .addInterceptor(cacheInterceptor)
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