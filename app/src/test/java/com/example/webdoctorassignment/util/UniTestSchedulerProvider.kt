package com.example.webdoctorassignment.util

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.internal.schedulers.TrampolineScheduler

class UniTestSchedulerProvider: ISchedulerProvider {
    override fun background(): Scheduler = TrampolineScheduler.instance()
    override fun main(): Scheduler = TrampolineScheduler.instance()
}