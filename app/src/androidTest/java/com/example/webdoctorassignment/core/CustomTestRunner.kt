package com.example.webdoctorassignment.core

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.squareup.rx3.idler.Rx3Idler
import dagger.hilt.android.testing.HiltTestApplication
import io.reactivex.rxjava3.plugins.RxJavaPlugins

class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, appName: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }

    override fun onStart() {
        super.onStart()
        RxJavaPlugins.setInitComputationSchedulerHandler(
            Rx3Idler.create("RxJava 3.x Computation Scheduler"));
        RxJavaPlugins.setInitIoSchedulerHandler(
            Rx3Idler.create("RxJava 3.x IO Scheduler"));
    }
}
