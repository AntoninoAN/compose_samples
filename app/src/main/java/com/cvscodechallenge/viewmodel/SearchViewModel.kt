package com.cvscodechallenge.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cvscodechallenge.model.Item
import com.cvscodechallenge.network.SearchRepository
import com.cvscodechallenge.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "SearchViewModel"

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {
    private val _searchLiveData = MutableStateFlow<UIState>(UIState.EMPTY)
    val searchLiveData: StateFlow<UIState>
        get() = _searchLiveData

    private val _itemDetailsLiveData = MutableStateFlow<UIState>(UIState.EMPTY)
    val itemDetailsLiveData: StateFlow<UIState>
        get() = _itemDetailsLiveData



    fun getSearch(tags: String) {
        Log.d(TAG, "getSearch: $tags")
        viewModelScope.launch {
            repository.getSearch(tags).collect {
                Log.d(TAG, "getSearch: $it")
                _searchLiveData.value = it
            }
            repository.insertNewTerm(tags)
        }
    }

    fun getDetailsItem(position: Int){
        viewModelScope.launch {
            repository.getDetailsListItem(position).collect {
                _itemDetailsLiveData.value = it
            }
        }
    }

    fun getPreviousSearch(){
        viewModelScope.launch {
            repository.getSearchTerms().collect {
                _searchLiveData.value = it
            }
        }
    }
}