package com.constantinemars.okhttpcachingpoc.rx

import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class Transformers {
    companion object {
        fun <T> applySchedulers() = FlowableTransformer<T, T> {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        }
    }
}