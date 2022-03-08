package com.cvscodechallenge.utils.di

import android.content.Context
import com.cvscodechallenge.network.APIService
import com.cvscodechallenge.network.SearchRepository
import com.cvscodechallenge.viewmodel.SearchViewModel
import com.cvscodechallenge.viewmodel.SearchViewModelFactory

class SearchInjector(private val context: Context) {

    private val repository: SearchRepository by lazy {
        SearchRepository(APIService.getRetrofit(), context)
    }

    private fun provideSearchViewModelFactory() = SearchViewModelFactory(repository = repository)

    val viewModel: SearchViewModel by lazy {
        provideSearchViewModelFactory().create(SearchViewModel::class.java)
    }
}