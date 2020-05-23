package com.constantinemars.okhttpcachingpoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.constantinemars.okhttpcachingpoc.model.GetResponse
import com.constantinemars.okhttpcachingpoc.rx.Transformers
import com.constantinemars.okhttpcachingpoc.rx.Transformers.Companion.applySchedulers
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())

        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        getButton.setOnClickListener {
            Timber.d("calling \"get\"")
            compositeDisposable.add(apiService.get()
                .compose(applySchedulers())
                .subscribe(
                    {response -> Timber.d(response.toString())},
                    {error -> Timber.e(error)}
                ))
        }

        postButton.setOnClickListener {
            Timber.d("calling \"post\"")
            compositeDisposable.add(apiService.post("long test data string")
                .compose(applySchedulers())
                .subscribe(
                    {response -> Timber.d(response.toString())},
                    {error -> Timber.e(error)}
                ))
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
