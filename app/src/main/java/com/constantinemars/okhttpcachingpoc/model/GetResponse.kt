package com.constantinemars.okhttpcachingpoc.model

import okhttp3.internal.format

class GetResponse(private val headers: Headers) {
    override fun toString() = format("[Response:\n" +
            "headers: %s\n" +
            "]",
        headers.toString())
}