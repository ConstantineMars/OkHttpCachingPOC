package com.constantinemars.okhttpcachingpoc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.constantinemars.okhttpcachingpoc.model.GetResponse
import com.constantinemars.okhttpcachingpoc.model.PostResponse
import com.constantinemars.okhttpcachingpoc.rx.Transformers.Companion.applySchedulers
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.HttpException
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val compositeDisposable by lazy {  CompositeDisposable() }
    private val cachedApiService by lazy {  ApiClient.cachedRetrofit.create(ApiService::class.java) }
    private val noCacheApiService by lazy {  ApiClient.noCacheRetrofit.create(ApiService::class.java) }

    //region Core
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        getButton.setOnClickListener {
            Timber.d("calling \"get\"")
            callGetCached()
        }

        postButton.setOnClickListener {
            Timber.d("calling \"post\"")
            callPostCached("long test data string")
        }
    }
    //endregion

    //region GET
    private fun callGetCached() {
        compositeDisposable.add(callGet(cachedApiService)
            .subscribe(
                {},
                { error -> if(error is HttpException) {
                    when(error.code()) {
                        504 -> callGetNoCache()
                    }
                    }
                }
            )
        )
    }

    private fun callGetNoCache() {
        compositeDisposable.add(callGet(noCacheApiService)
            .subscribe()
        )
    }

    private fun callGet(apiService: ApiService): Flowable<GetResponse> =
        apiService.get()
            .compose(applySchedulers())
            .doOnNext { response -> Timber.d(response.toString()) }
            .doOnError { error -> Timber.e(error) }
    //endregion

    //region POST
    private fun callPostCached(data: String) {
        compositeDisposable.add(callPost(cachedApiService, data)
            .subscribe(
                {},
                { error -> if(error is HttpException) {
                    when(error.code()) {
                        504 -> callPostNoCache(data)
                    }
                }
                }
            )
        )
    }

    private fun callPostNoCache(data: String) {
        compositeDisposable.add(callPost(noCacheApiService, data)
            .subscribe()
        )
    }

    private fun callPost(apiService: ApiService, data: String): Flowable<PostResponse> =
        apiService.post(data)
            .compose(applySchedulers())
            .doOnNext { response -> Timber.d(response.toString())}
            .doOnError { error -> Timber.e(error) }
    //endregion

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
