package com.arctouch.codechallenge.util.schedulers

import androidx.annotation.NonNull
import io.reactivex.Scheduler


interface BaseScheduler {

    @NonNull
    fun io(): Scheduler

    @NonNull
    fun ui(): Scheduler

}