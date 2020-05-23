package com.constantinemars.okhttpcachingpoc.model

import okhttp3.internal.format

class PostResponse(private val data: String, private val headers: Headers) {
    override fun toString() = format("[Response:\n" +
            "data: %s\n" +
            "headers: %s\n]",
        data,
        headers.toString())
}