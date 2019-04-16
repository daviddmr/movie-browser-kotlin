package com.arctouch.codechallenge.util.schedulers

import android.support.annotation.NonNull
import io.reactivex.Scheduler


interface BaseScheduler {

    @NonNull
    fun io(): Scheduler

    @NonNull
    fun ui(): Scheduler

}