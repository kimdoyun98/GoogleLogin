package com.project.googleloginguide

import android.app.Application
import android.content.Context
import com.project.googleloginguide.di.AppContainer

class MyApplication: Application() {
    val container by lazy { AppContainer(this) }
}

val Context.appContainer: AppContainer get() = (applicationContext as MyApplication).container
