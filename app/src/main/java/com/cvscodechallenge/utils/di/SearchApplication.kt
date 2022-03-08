package com.cvscodechallenge.utils.di

import android.app.Application
import android.content.Context

class SearchApplication: Application() {

    lateinit var searchInjector: SearchInjector

    override fun onCreate() {
        super.onCreate()
        searchInjector = SearchInjector(applicationContext)
    }
}