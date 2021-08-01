package br.com.mecury.movieapp

import android.app.Application
import br.com.mecury.movieapp.di.appModules
import br.com.mecury.movieapp.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppApplication)
            modules(arrayListOf(appModules, viewModels))
        }

    }

}