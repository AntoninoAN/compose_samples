package com.example.tmobiletest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tmobiletest.repository.Repository
import com.example.tmobiletest.repository.UIState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class CardViewModel(private val repository: Repository): ViewModel() {

    private val _cards = MutableLiveData<UIState>()
    val cards: LiveData<UIState>
    get() = _cards

    private val coroutineScope = CoroutineScope(Job()+Dispatchers.IO)

    init {
        coroutineScope.launch {
            repository.getCards().collect {
                withContext(Dispatchers.Main){ _cards.value = it }
            }
        }
    }
}