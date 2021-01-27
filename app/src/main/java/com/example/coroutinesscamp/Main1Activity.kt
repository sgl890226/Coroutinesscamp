package com.example.coroutinesscamp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hencoder.coroutinescamp.Api
import com.hencoder.coroutinescamp.model.Repo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main1.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.function.BiFunction
import kotlin.concurrent.thread

/**
 * 课程2
 */
class Main1Activity : AppCompatActivity() {
    var job:Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        //---------------------------请求单个接口-----------------------
        /**
         *  retrofit对协程的写法
         */
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(Api::class.java)
//        GlobalScope.launch(Dispatchers.Main) {
//            try {
//                val repos = api.listReposKt("sgl890226") // 后台
//                tvTextview1.text = repos[0].name // 前台
//            } catch (e: Exception) {
//                tvTextview1.text = e.message // 前台
//            }
//        }
        /**
         * Rxjava 的写法
         */
        val retrofitRxJava = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
        val api1 = retrofitRxJava.create(Api::class.java)
//        api1.listReposRx("sgl890226")
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : SingleObserver<List<Repo>> {
//                override fun onSuccess(repos: List<Repo>) {
//                    tvTextview2.text = repos[0].name // 前台
//                }
//
//                override fun onSubscribe(d: Disposable) {
//                }
//
//                override fun onError(e: Throwable) {
//                    tvTextview2.text = e.message // 前台
//                }
//            })
        //---------------------------请求多个接口-----------------------
        /**
         * 普通 的写法
         */
        /*api.listRepos("sgl890226")
      .enqueue(object : Callback<List<Repo>?> {
        override fun onResponse(call: Call<List<Repo>?>, response: Response<List<Repo>?>) {
          val namesgl890226 = response.body()?.get(0)?.name
          api.listRepos("google")
            .enqueue(object : Callback<List<Repo>?> {
              override fun onResponse(call: Call<List<Repo>?>, response: Response<List<Repo>?>) {
                tvTextview2.text = "${namesgl890226}-${response.body()?.get(0)?.name}"
              }

              override fun onFailure(call: Call<List<Repo>?>, t: Throwable) {
              }
            })
        }

        override fun onFailure(call: Call<List<Repo>?>, t: Throwable) {

        }
      })*/
        /**
         * Rxjava 的写法
         */

//        Single.zip(
//            api1.listReposRx("sgl890226"),
//            api1.listReposRx("google"),
//            BiFunction { list1, list2 -> "${list1[0].name} - ${list2[0].name}" }
//        ).observeOn(AndroidSchedulers.mainThread())
//            .subscribe { combined -> tvTextview2.text = combined }

        /**
         * 协程 的写法
         */
        //使用该方式记得destory 取消当前任务，防止内测泄露
        job =  GlobalScope.launch(Dispatchers.Main) {
            val one = async { api.listReposKt("sgl890226") }
            val two = async { api.listReposKt("google") }
            tvTextview1.text = "${one.await()[0].name} -> ${two.await()[0].name}"
        }
        // 使用该方式不需要destory取消当前任务，android帮我们做了。
        lifecycleScope.launchWhenCreated {
            val one = async { api.listReposKt("sgl890226") }
            val two = async { api.listReposKt("google") }
            tvTextview2.text = "${one.await()[0].name} -> ${two.await()[0].name}"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}
