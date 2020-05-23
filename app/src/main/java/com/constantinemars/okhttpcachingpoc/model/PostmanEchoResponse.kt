package com.constantinemars.okhttpcachingpoc.model

import okhttp3.internal.format

class PostmanEchoResponse(private val headers: Headers) {
    override fun toString() = format("[Response:\n%s\n]", headers.toString())
}