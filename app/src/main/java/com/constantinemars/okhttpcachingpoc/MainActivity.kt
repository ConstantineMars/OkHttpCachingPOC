package com.constantinemars.okhttpcachingpoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.constantinemars.okhttpcachingpoc.model.GetResponse
import com.constantinemars.okhttpcachingpoc.model.PostResponse
import com.constantinemars.okhttpcachingpoc.rx.Transformers
import com.constantinemars.okhttpcachingpoc.rx.Transformers.Companion.applySchedulers
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.HttpException
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val compositeDisposable by lazy {  CompositeDisposable() }
    private val cachedApiService by lazy {  ApiClient.cachedRetrofit.create(ApiService::class.java) }
    private val noCacheApiService by lazy {  ApiClient.noCacheRetrofit.create(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        getButton.setOnClickListener {
            Timber.d("calling \"get\"")
            callGetCached()
//            compositeDisposable.add(cachedApiService.get()
//                .compose(applySchedulers())
//                .subscribe(
//                    {response -> Timber.d(response.toString())},
//                    {error -> Timber.e(error) }
//                ))
        }

        postButton.setOnClickListener {
            Timber.d("calling \"post\"")
            compositeDisposable.add(cachedApiService.post("long test data string")
                .compose(applySchedulers())
                .subscribe(
                    {response -> Timber.d(response.toString())},
                    {error -> Timber.e(error) }
                ))
        }
    }

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

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
