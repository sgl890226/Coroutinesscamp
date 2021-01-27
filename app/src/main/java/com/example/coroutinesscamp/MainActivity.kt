package com.example.coroutinesscamp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

/**
 * 课程1
 */
class MainActivity : AppCompatActivity() {
    var strValue:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        GlobalScope.launch {
//            println("Coroutines Camp 1 :${Thread.currentThread().name}")
//        }
//        Thread {
//            println("Coroutines Camp 2 :${Thread.currentThread().name}")
//        }.start()
//
//        thread {
//            println("Coroutines Camp 2 :${Thread.currentThread().name}")
//        }
        /**
         * 下面我们描述一个常见的场景  后台耗时操作 -> 前台刷新界面 -> 后台耗时操作 -> 前台刷新界面 等操作 就是来回切线程做操作
         * ioCamp1() //后台耗时操作
        uiCamp1() //前台刷新界面
        ioCamp2() //后台耗时操作
        uiCamp2() //前台刷新界面
        ioCamp3() //后台耗时操作
        uiCamp3() //前台刷新界面
         */
        /**
         * java 的做法。
         */
//        thread { //子线程
//            ioCamp4()
//            runOnUiThread { //主线程
//                uiCamp4()
//                thread { //子线程
//                    ioCamp4()
//                    runOnUiThread {//主线程
//                        uiCamp4()
//                    }
//                }
//            }
//        }
        /**
         * kotion 协程 的做法。
         *
         * suspend : 挂起函数，只起标记作用，实际没有作用
         *    并不是用来切线程的
         *    关键作用：标记和提醒
         *
         *
         * Dispatchers.Main：指定线程
         * 用 launch（）来开启一段协程 一般需要指定Dispatchers.Main
         * 把要在后台工作的函数，写成supend函数，需要在内部调用其他suspend函数真正切换线程
         *
         * 协程提高软件性能
         * 程序什么时候需要切换线程
         *  1.工作比较耗时：放在后台
         *      a）怎么判断函数是否耗时
         *      b）如何使用协程规避这个问题 --- 写的时候把该函数写成挂起函数。
         *  2.工作比较特殊：放在指定线程 -- 一般是主线程（譬如更新ui）
         *
         * 亮点1：耗时函数自动后台，从而提高性能。
         * 亮点2：线程的自动切回
         *
         * 协程提供了挂起函数所需要的上下文信息。
         */

        GlobalScope.launch(Dispatchers.Main) {
            ioCamp1()
            uiCamp1()
            ioCamp2()
            uiCamp2()
        }

    }


    private suspend fun ioCamp1() {
        withContext(Dispatchers.IO) { //挂起函数，切换到子线程
            strValue = "携程的那个"

            Log.e("Coroutines", "io1:${Thread.currentThread().name}")
        }
    }

    private fun uiCamp1() {
        tvTextview1.text = strValue
        Log.e("Coroutines", "ui1:${Thread.currentThread().name}")
    }

    private suspend fun ioCamp2() {
        withContext(Dispatchers.IO) { //挂起函数，切换到子线程
            Log.e("Coroutines", "io2:${Thread.currentThread().name}")
        }
    }

    private fun uiCamp2() {
        Log.e("Coroutines", "ui2:${Thread.currentThread().name}")
    }

    private fun ioCamp3() {
        Log.e("Coroutines", "io3:${Thread.currentThread().name}")
    }

    private fun uiCamp3() {
        Log.e("Coroutines", "ui3:${Thread.currentThread().name}")
    }


    private fun ioCamp4() {
        Log.e("Coroutines", "io4:${Thread.currentThread().name}")
    }

    private fun uiCamp4() {
        Log.e("Coroutines", "ui4:${Thread.currentThread().name}")
    }
}
