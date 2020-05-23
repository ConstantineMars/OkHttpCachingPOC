package com.constantinemars.okhttpcachingpoc.model

import com.google.gson.annotations.SerializedName
import okhttp3.internal.format

class Headers(
    val host: String,
    @SerializedName("cache-control") val cacheControl: String
) {
    override fun toString() = format(
                    "[\n" +
                            "host: %s\n" +
                    "cacheControl: %s\n" +
                            "]",
        host,
        cacheControl)
}